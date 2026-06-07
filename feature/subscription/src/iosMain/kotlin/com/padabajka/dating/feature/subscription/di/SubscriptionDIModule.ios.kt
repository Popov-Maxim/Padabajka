package com.padabajka.dating.feature.subscription.di

import com.padabajka.dating.feature.subscription.data.billing.IosBillingClient
import com.padabajka.dating.feature.subscription.data.billing.PlatformBillingClient
import com.padabajka.dating.feature.subscription.data.billing.SKProductMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformSubDiModule: Module
    get() = module {
        singleOf(::IosBillingClient) {
            bind<PlatformBillingClient>()
        }

        factoryOf(::SKProductMapper)
    }
