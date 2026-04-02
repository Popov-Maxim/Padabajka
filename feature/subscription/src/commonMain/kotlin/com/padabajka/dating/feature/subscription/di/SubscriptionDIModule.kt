package com.padabajka.dating.feature.subscription.di

import com.padabajka.dating.core.repository.api.SubscriptionRepository
import com.padabajka.dating.feature.subscription.data.SubscriptionRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val subscriptionDIModule = module {
    singleOf(::SubscriptionRepositoryImpl) {
        bind<SubscriptionRepository>()
    }
}
