package com.padabajka.dating.feature.subscription.data.source

import com.padabajka.dating.core.data.network.incoming.dto.FeatureUsageDto
import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionPlanDto
import kotlinx.coroutines.flow.Flow

interface LocalSubscriptionDataSource {
    val subscriptionPlan: Flow<SubscriptionPlanDto>
    val featureUsage: Flow<FeatureUsageDto>

    suspend fun updatePlan(update: (SubscriptionPlanDto) -> SubscriptionPlanDto)
    suspend fun updateFeatureUsage(update: (FeatureUsageDto) -> FeatureUsageDto)
}
