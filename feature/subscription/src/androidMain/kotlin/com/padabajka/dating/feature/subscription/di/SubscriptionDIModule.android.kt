package com.padabajka.dating.feature.subscription.di

import com.padabajka.dating.feature.subscription.data.billing.AndroidBillingClient
import com.padabajka.dating.feature.subscription.data.billing.BillingProductMapper
import com.padabajka.dating.feature.subscription.data.billing.PlatformBillingClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformSubDiModule: Module = module {
    singleOf(::AndroidBillingClient) {
        bind<PlatformBillingClient>()
    }

    factoryOf(::BillingProductMapper)
}
