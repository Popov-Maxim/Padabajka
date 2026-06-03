package com.padabajka.dating.feature.subscription.data.billing

import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionProduct

interface PlatformBillingClient {
    val serviceName: ServiceName

    suspend fun getSubscriptions(): List<SubscriptionProduct>

    suspend fun launchPurchase(offerId: String): PurchaseResult?

    enum class ServiceName(val raw: String) {
        GOOGLE_PLAY("google_play"),
        APP_STORE("app_store")
    }
}
