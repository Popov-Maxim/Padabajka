package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.subscription.FeatureUsage
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    val subscriptionState: Flow<SubscriptionState>
    val subscriptionStateValue: SubscriptionState

    suspend fun update(update: (FeatureUsage) -> FeatureUsage)

    suspend fun syncState()
    suspend fun subscribe()
}
