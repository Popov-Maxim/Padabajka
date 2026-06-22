package com.padabajka.dating.feature.subscription.data.billing

data class PurchaseResult(
    val productId: String,
    val purchaseToken: String? = null,
)
