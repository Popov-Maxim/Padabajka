package com.padabajka.dating.feature.subscription.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState
import com.padabajka.dating.feature.subscription.data.SubscriptionRepositoryImpl
import com.padabajka.dating.feature.subscription.data.network.SubscriptionApi
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.LocalSubscriptionDataSourceImpl
import com.padabajka.dating.feature.subscription.data.source.RemoteSubscriptionDataSource
import com.padabajka.dating.feature.subscription.data.source.model.SubscriptionStateDto
import com.padabajka.dating.feature.subscription.data.source.model.toDto
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
            datastore = DataStoreUtils.create(
                "subscription_datastore",
                SubscriptionStateDto.serializer(),
                SubscriptionState.DEFAULT.toDto(),
                true
            )
        )
    }
}
