package com.padabajka.dating.feature.subscription.data

import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionStateResponse
import com.padabajka.dating.core.data.network.incoming.dto.toDomain
import com.padabajka.dating.core.data.network.incoming.dto.toDto
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import com.padabajka.dating.core.repository.api.exception.SubscriptionPurchaseException
import com.padabajka.dating.core.repository.api.exception.UserException
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.repository.api.model.subscription.FeatureUsage
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionPlan
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionPurchaseResult
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState
import com.padabajka.dating.feature.subscription.data.billing.PlatformBillingClient
import com.padabajka.dating.feature.subscription.data.billing.PurchaseResult
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.PendingSubscriptionPurchaseDataSource
import com.padabajka.dating.feature.subscription.data.source.RemoteSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.model.PendingSubscriptionPurchaseDto
import com.padabajka.dating.feature.subscription.data.source.model.PurchaseRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class SubscriptionRepositoryImpl(
    coroutineScope: CoroutineScope,
    private val localSubscriptionDataSource: LocalSubscriptionDataSource,
    private val remoteSubscriptionDataSource: RemoteSubscriptionDataSource,
    private val platformBillingClient: PlatformBillingClient,
    private val pendingPurchaseDataSource: PendingSubscriptionPurchaseDataSource,
    private val authRepository: AuthRepository,
) : SubscriptionRepository {

    private val _subscriptionState =
        combine(
            localSubscriptionDataSource.subscriptionPlan,
            localSubscriptionDataSource.featureUsage
        ) { plan, usage ->
            SubscriptionState(
                isActive = plan.isActive,
                features = SubscriptionState.Features(
                    showLikes = plan.showLikes,
                    hideAds = plan.hideAds,
                    superLikes = (plan.superLikes - usage.superLikes).coerceAtLeast(0),
                    returns = (plan.returns - usage.returns).coerceAtLeast(0)
                )
            )
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    override val subscriptionState: Flow<SubscriptionState>
        get() = _subscriptionState.filterNotNull()
    override val subscriptionStateValue: SubscriptionState
        get() = _subscriptionState.value ?: SubscriptionState.DEFAULT

    private val _subscriptionProducts: MutableStateFlow<List<SubscriptionProduct>> =
        MutableStateFlow(listOf())
    override val subscriptionProducts: Flow<List<SubscriptionProduct>>
        get() = _subscriptionProducts.asStateFlow()
    override val subscriptionProductsValue: List<SubscriptionProduct>
        get() = _subscriptionProducts.value

    override suspend fun update(update: (FeatureUsage) -> FeatureUsage) {
        localSubscriptionDataSource.updateFeatureUsage {
            update(it.toDomain()).toDto()
        }
    }

    override suspend fun syncState() {
        val subscriptionStateResponse = remoteSubscriptionDataSource.loadSubscriptionState()
        updateSubscriptionState(subscriptionStateResponse)

        val subProducts = platformBillingClient.getSubscriptions()
        _subscriptionProducts.value = subProducts
    }

    override suspend fun subscribe(offerId: String): SubscriptionPurchaseResult {
        val userId = authRepository.currentAuthState.userIdOrNull()?.raw ?: throw UserException.Unauthorized()
        val pendingPurchase = withSubscriptionError(::SubscriptionPurchaseException) {
            pendingPurchaseDataSource.get(userId, offerId)
        } ?: when (
            val result = withSubscriptionError(::SubscriptionPurchaseException) {
                platformBillingClient.launchPurchase(offerId)
            }
        ) {
            is PurchaseResult.Purchased -> savePurchase(userId, offerId, result)
            PurchaseResult.Cancelled -> return SubscriptionPurchaseResult.Cancelled
        }

        withSubscriptionError(SubscriptionException::ActivationFailed) {
            val subscriptionStateResponse = remoteSubscriptionDataSource.subscribe(pendingPurchase.request)
            removePendingPurchases(userId)
            updateSubscriptionState(subscriptionStateResponse)
        }
        return SubscriptionPurchaseResult.Purchased
    }

    override suspend fun clearLocalData() {
        localSubscriptionDataSource.updatePlan {
            SubscriptionPlan.DEFAULT.toDto()
        }
        localSubscriptionDataSource.updateFeatureUsage {
            FeatureUsage.DEFAULT.toDto()
        }
    }

    private suspend fun savePurchase(
        userId: String,
        offerId: String,
        result: PurchaseResult.Purchased,
    ): PendingSubscriptionPurchaseDto {
        return withSubscriptionError(SubscriptionException::ActivationFailed) {
            val purchaseToken = result.purchaseToken?.takeIf { it.isNotBlank() }
                ?: throw SubscriptionException.RecoveryFailed()
            val request = PurchaseRequest(
                productId = result.productId,
                purchaseToken = purchaseToken,
                service = platformBillingClient.serviceName.raw
            )
            val pendingPurchase = PendingSubscriptionPurchaseDto(
                userId = userId,
                request = request,
                offerId = offerId,
            )
            withContext(NonCancellable) {
                pendingPurchaseDataSource.save(pendingPurchase)
            }

            pendingPurchase
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun <T> withSubscriptionError(
        fallback: (Throwable) -> SubscriptionException,
        action: suspend () -> T,
    ): T = try {
        action()
    } catch (error: Exception) {
        throw when (error) {
            is CancellationException, is SubscriptionException, is UserException -> error
            else -> fallback(error)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun removePendingPurchases(userId: String) {
        withContext(NonCancellable) {
            try {
                pendingPurchaseDataSource.removeAll(userId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                // Do not log the exception message: it may contain the purchase token.
                println("Failed to remove pending subscription purchases: ${error::class.simpleName}")
            }
        }
    }

    private suspend fun updateSubscriptionState(subscriptionStateResponse: SubscriptionStateResponse) {
        localSubscriptionDataSource.updatePlan {
            subscriptionStateResponse.plan
        }
        localSubscriptionDataSource.updateFeatureUsage {
            subscriptionStateResponse.featureUsage
        }
    }
}
