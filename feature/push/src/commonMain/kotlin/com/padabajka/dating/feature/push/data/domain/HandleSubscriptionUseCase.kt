package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.DataPush
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSource

class HandleSubscriptionUseCase(
    private val localSubscriptionDataSource: LocalSubscriptionDataSource
) {
    suspend operator fun invoke(dataPush: DataPush.FeatureUsage) {
        localSubscriptionDataSource.updateFeatureUsage {
            dataPush.featureUsage
        }
    }

    suspend operator fun invoke(dataPush: DataPush.SubscriptionPlan) {
        localSubscriptionDataSource.updatePlan {
            dataPush.plan
        }
    }
}
