package com.padabajka.dating.di

import com.padabajka.dating.feature.subscription.data.billing.ObserveStoreKit2Entitlements
import com.padabajka.dating.feature.subscription.data.billing.StoreKit2Client
import org.koin.dsl.module

fun initKoinIos(observeCurrentEntitlements: ObserveStoreKit2Entitlements) {
    initKoin(
        module {
            single { StoreKit2Client(observeCurrentEntitlements) }
        }
    )
}
