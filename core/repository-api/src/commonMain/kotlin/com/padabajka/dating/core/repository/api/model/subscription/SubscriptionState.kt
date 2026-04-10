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

    companion object {
        val DEFAULT = SubscriptionState(
            isActive = false,
            features = Features(
                showLikes = false,
                superLikes = 0,
                returns = 0
            )
        )
    }
}
