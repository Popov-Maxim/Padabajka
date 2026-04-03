package com.padabajka.dating.feature.subscription.data.source

import com.padabajka.dating.feature.subscription.data.source.model.SubscriptionStateDto
import kotlinx.coroutines.flow.Flow

interface LocalSubscriptionDataSource {
    val subscriptionState: Flow<SubscriptionStateDto>

    suspend fun update(update: (SubscriptionStateDto) -> SubscriptionStateDto)
}
