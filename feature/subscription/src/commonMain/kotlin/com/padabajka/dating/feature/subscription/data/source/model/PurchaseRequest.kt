package com.padabajka.dating.feature.subscription.data.source.model

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequest(
    val purchaseToken: String,
    val service: String,
)
