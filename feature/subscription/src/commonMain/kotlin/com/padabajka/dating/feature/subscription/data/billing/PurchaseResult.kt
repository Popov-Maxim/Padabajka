package com.padabajka.dating.feature.subscription.data.billing

sealed interface PurchaseResult {
    data class Purchased(
        val productId: String,
        val purchaseToken: String? = null,
    ) : PurchaseResult

    data object Cancelled : PurchaseResult
}
