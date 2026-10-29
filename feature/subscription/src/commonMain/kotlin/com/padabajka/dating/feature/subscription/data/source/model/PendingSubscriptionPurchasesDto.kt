package com.padabajka.dating.feature.subscription.data.source.model

import kotlinx.serialization.Serializable

@Serializable
data class PendingSubscriptionPurchasesDto(
    val purchases: List<PendingSubscriptionPurchaseDto> = emptyList()
)

@Serializable
data class PendingSubscriptionPurchaseDto(
    val userId: String,
    val request: PurchaseRequest,
)
