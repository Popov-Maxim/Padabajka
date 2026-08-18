package com.padabajka.dating.core.repository.api.exception

sealed class SuperLikeException(message: String) : Exception(message) {
    class LimitReached : SuperLikeException("Super Like limit reached")
    class SubscriptionRequired : SuperLikeException("Super Like requires an active subscription")

    companion object {
        const val LIMIT_REACHED_CODE = "SUPER_LIKE_LIMIT_REACHED"
        const val SUBSCRIPTION_REQUIRED_CODE = "SUPER_LIKE_SUBSCRIPTION_REQUIRED"
    }
}
