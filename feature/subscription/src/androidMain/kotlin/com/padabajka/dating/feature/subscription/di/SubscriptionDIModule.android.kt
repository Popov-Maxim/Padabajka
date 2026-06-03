package com.padabajka.dating.feature.subscription.di

import com.padabajka.dating.feature.subscription.data.billing.AndroidBillingClient
import com.padabajka.dating.feature.subscription.data.billing.BillingProductMapper
import com.padabajka.dating.feature.subscription.data.billing.PlatformBillingClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val platformSubDiModule: Module = module {
    single<PlatformBillingClient> {
        AndroidBillingClient(
            context = get(),
            billingProductMapper = get()
        )
    }

    factoryOf(::BillingProductMapper)
}
