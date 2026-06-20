package com.padabajka.dating.feature.subscription.data.source.model

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequest(
    val productId: String,
    val purchaseToken: String,
    val service: String,
)
