package com.padabajka.dating.core.repository.api.model.subscription

data class FeatureUsage(
    val returns: Int,
    val superLikes: Int
) {
    companion object {
        val DEFAULT = FeatureUsage(
            returns = 0,
            superLikes = 0
        )
    }
}
