package com.padabajka.dating.feature.subscription.data

import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionStateResponse
import com.padabajka.dating.core.data.network.incoming.dto.toDomain
import com.padabajka.dating.core.data.network.incoming.dto.toDto
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.subscription.FeatureUsage
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.RemoteSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.model.PurchaseRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn

class SubscriptionRepositoryImpl(
    coroutineScope: CoroutineScope,
    private val localSubscriptionDataSource: LocalSubscriptionDataSource,
    private val remoteSubscriptionDataSource: RemoteSubscriptionDataSource
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

    override suspend fun update(update: (FeatureUsage) -> FeatureUsage) {
        localSubscriptionDataSource.updateFeatureUsage {
            update(it.toDomain()).toDto()
        }
    }

    override suspend fun syncState() {
        val subscriptionStateResponse = remoteSubscriptionDataSource.loadSubscriptionState()
        updateSubscriptionState(subscriptionStateResponse)
    }

    override suspend fun subscribe() {
        val subscriptionStateResponse = remoteSubscriptionDataSource.subscribe(
            PurchaseRequest(
                purchaseToken = "todo",
                service = "todo"
            )
        )
        updateSubscriptionState(subscriptionStateResponse)
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
