package com.padabajka.dating.core.data.network.incoming.dto

import kotlinx.serialization.Serializable

@Serializable
class SubscriptionStateResponse(
    val isActive: Boolean,
    val features: Features
) {
    @Serializable
    data class Features(
        val showLikes: Boolean,
        val superLikes: Int,
        val returns: Int
    )
}
