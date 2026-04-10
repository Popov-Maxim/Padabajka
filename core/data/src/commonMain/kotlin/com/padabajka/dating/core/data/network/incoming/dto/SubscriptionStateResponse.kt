package com.padabajka.dating.core.data.network.incoming.dto

import kotlinx.serialization.Serializable

@Serializable
class SubscriptionStateResponse(
    val plan: SubscriptionPlanDto,
    val featureUsage: FeatureUsageDto
)
