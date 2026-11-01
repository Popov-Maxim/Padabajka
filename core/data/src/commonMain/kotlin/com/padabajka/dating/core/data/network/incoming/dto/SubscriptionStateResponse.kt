package com.padabajka.dating.core.data.network.incoming.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SubscriptionStateResponse {
    @Serializable
    @SerialName("success")
    data class Success(
        val plan: SubscriptionPlanDto,
        val featureUsage: FeatureUsageDto
    ) : SubscriptionStateResponse()

    @Serializable
    @SerialName("failed")
    data class Failed(
        val code: String,
        val message: String
    ) : SubscriptionStateResponse()
}
