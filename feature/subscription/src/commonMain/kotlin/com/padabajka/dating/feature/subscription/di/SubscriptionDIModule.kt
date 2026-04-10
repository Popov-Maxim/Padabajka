package com.padabajka.dating.feature.subscription.di

import com.padabajka.dating.core.data.network.incoming.dto.FeatureUsageDto
import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionPlanDto
import com.padabajka.dating.core.data.network.incoming.dto.toDto
import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.subscription.FeatureUsage
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionPlan
import com.padabajka.dating.feature.subscription.data.SubscriptionRepositoryImpl
import com.padabajka.dating.feature.subscription.data.network.SubscriptionApi
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSourceImpl
import com.padabajka.dating.feature.subscription.data.source.RemoteSubscriptionDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val subscriptionDIModule = module {
    singleOf(::SubscriptionRepositoryImpl) {
        bind<SubscriptionRepository>()
    }

    factoryOf(::RemoteSubscriptionDataSource)
    factoryOf(::SubscriptionApi)
    single<LocalSubscriptionDataSource> {
        LocalSubscriptionDataSourceImpl(
            planDatastore = DataStoreUtils.create(
                "subscription_plan_datastore",
                SubscriptionPlanDto.serializer(),
                SubscriptionPlan.DEFAULT.toDto(),
                true
            ),
            usageDatastore = DataStoreUtils.create(
                "feature_usage_datastore",
                FeatureUsageDto.serializer(),
                FeatureUsage.DEFAULT.toDto(),
                true
            ),
        )
    }
}
