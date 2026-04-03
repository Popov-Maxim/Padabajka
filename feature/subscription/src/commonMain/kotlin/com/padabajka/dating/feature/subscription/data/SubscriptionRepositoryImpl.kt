package com.padabajka.dating.feature.subscription.data

import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.RemoteSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.model.PurchaseRequest
import com.padabajka.dating.feature.subscription.data.source.model.toDomain
import com.padabajka.dating.feature.subscription.data.source.model.toDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SubscriptionRepositoryImpl(
    coroutineScope: CoroutineScope,
    private val localSubscriptionDataSource: LocalSubscriptionDataSource,
    private val remoteSubscriptionDataSource: RemoteSubscriptionDataSource
) : SubscriptionRepository {

    private val _subscriptionState = localSubscriptionDataSource.subscriptionState
        .map {
            it.toDomain()
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    override val subscriptionState: Flow<SubscriptionState>
        get() = _subscriptionState.filterNotNull()
    override val subscriptionStateValue: SubscriptionState
        get() = _subscriptionState.value ?: SubscriptionState.DEFAULT

    override suspend fun update(update: (SubscriptionState) -> SubscriptionState) {
        localSubscriptionDataSource.update {
            update(it.toDomain()).toDto()
        }
    }

    override suspend fun syncState() {
        val subscriptionStateResponse = remoteSubscriptionDataSource.loadSubscriptionState()
        localSubscriptionDataSource.update {
            subscriptionStateResponse.toDto()
        }
    }

    override suspend fun subscribe() {
        val subscriptionStateResponse = remoteSubscriptionDataSource.subscribe(
            PurchaseRequest(
                purchaseToken = "todo",
                service = "todo"
            )
        )
        localSubscriptionDataSource.update {
            subscriptionStateResponse.toDto()
        }
    }
}
