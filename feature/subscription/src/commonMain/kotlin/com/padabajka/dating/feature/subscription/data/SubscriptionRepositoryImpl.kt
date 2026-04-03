package com.padabajka.dating.feature.subscription.data

import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SubscriptionRepositoryImpl : SubscriptionRepository {

    private val _subscriptionState = MutableStateFlow(defaultState)
    override val subscriptionState: Flow<SubscriptionState>
        get() = _subscriptionState
    override val subscriptionStateValue: SubscriptionState
        get() = _subscriptionState.value

    override suspend fun update(update: (SubscriptionState) -> SubscriptionState) {
        _subscriptionState.update(update)
    }

    override suspend fun loadState() {
        // TODO("Not yet implemented")
    }

    override suspend fun subscribe() {
        _subscriptionState.value = premiumState
    }

    private companion object {
        private val premiumState = SubscriptionState(
            isActive = true,
            features = SubscriptionState.Features(
                showLikes = true,
                superLikes = 5,
                returns = 5
            )
        )
        private val defaultState = SubscriptionState(
            isActive = false,
            features = SubscriptionState.Features(
                showLikes = false,
                superLikes = 0,
                returns = 0
            )
        )
    }
}
