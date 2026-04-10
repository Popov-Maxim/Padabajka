package com.padabajka.dating.feature.subscription.data.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.core.data.network.incoming.dto.FeatureUsageDto
import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionPlanDto
import kotlinx.coroutines.flow.Flow

class LocalSubscriptionDataSourceImpl(
    private val planDatastore: DataStore<SubscriptionPlanDto>,
    private val usageDatastore: DataStore<FeatureUsageDto>
) : LocalSubscriptionDataSource {
    override val subscriptionPlan: Flow<SubscriptionPlanDto>
        get() = planDatastore.data
    override val featureUsage: Flow<FeatureUsageDto>
        get() = usageDatastore.data

    override suspend fun updatePlan(update: (SubscriptionPlanDto) -> SubscriptionPlanDto) {
        planDatastore.updateData(update)
    }

    override suspend fun updateFeatureUsage(update: (FeatureUsageDto) -> FeatureUsageDto) {
        usageDatastore.updateData(update)
    }
}
