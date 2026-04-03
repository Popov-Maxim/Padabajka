package com.padabajka.dating.core.repository.api.model.subscription

data class SubscriptionState(
    val isActive: Boolean,
    val features: Features
) {
    data class Features(
        val showLikes: Boolean,
        val superLikes: Int,
        val returns: Int
    )
}

fun SubscriptionState.updateFeatures(update: (SubscriptionState.Features) -> SubscriptionState.Features) = copy(
    features = update(features)
)
