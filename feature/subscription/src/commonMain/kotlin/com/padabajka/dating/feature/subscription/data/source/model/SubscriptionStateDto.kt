package com.padabajka.dating.feature.subscription.data.source.model

import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionStateResponse
import com.padabajka.dating.core.repository.api.model.subscription.SubscriptionState
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionStateDto(
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

fun SubscriptionStateDto.toDomain(): SubscriptionState {
    return SubscriptionState(
        isActive = isActive,
        features = features.toDomain()
    )
}

private fun SubscriptionStateDto.Features.toDomain(): SubscriptionState.Features {
    return SubscriptionState.Features(
        showLikes = showLikes,
        superLikes = superLikes,
        returns = returns
    )
}

fun SubscriptionState.toDto(): SubscriptionStateDto {
    return SubscriptionStateDto(
        isActive = isActive,
        features = features.toDomain()
    )
}

private fun SubscriptionState.Features.toDomain(): SubscriptionStateDto.Features {
    return SubscriptionStateDto.Features(
        showLikes = showLikes,
        superLikes = superLikes,
        returns = returns
    )
}

fun SubscriptionStateResponse.toDto(): SubscriptionStateDto {
    return SubscriptionStateDto(
        isActive = isActive,
        features = features.toDto()
    )
}

private fun SubscriptionStateResponse.Features.toDto(): SubscriptionStateDto.Features {
    return SubscriptionStateDto.Features(
        showLikes = showLikes,
        superLikes = superLikes,
        returns = returns
    )
}
