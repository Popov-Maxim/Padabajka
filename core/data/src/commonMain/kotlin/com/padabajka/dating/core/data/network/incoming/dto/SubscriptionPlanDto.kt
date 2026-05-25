package com.padabajka.dating.core.data.network.incoming.dto

import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionPlan
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionPlanDto(
    val isActive: Boolean,
    val hideAds: Boolean,
    val showLikes: Boolean,
    val superLikes: Int,
    val returns: Int
)

fun SubscriptionPlanDto.toDomain(): SubscriptionPlan {
    return SubscriptionPlan(
        isActive = isActive,
        hideAds = hideAds,
        showLikes = showLikes,
        superLikes = superLikes,
        returns = returns
    )
}

fun SubscriptionPlan.toDto(): SubscriptionPlanDto {
    return SubscriptionPlanDto(
        isActive = isActive,
        hideAds = hideAds,
        showLikes = showLikes,
        superLikes = superLikes,
        returns = returns
    )
}
