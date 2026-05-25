package com.padabajka.dating.core.repository.api.model.subscription

data class SubscriptionPlan(
    val isActive: Boolean,
    val hideAds: Boolean,
    val showLikes: Boolean,
    val superLikes: Int,
    val returns: Int
) {
    companion object {
        val DEFAULT = SubscriptionPlan(
            isActive = false,
            hideAds = false,
            showLikes = false,
            superLikes = 0,
            returns = 0
        )
    }
}
