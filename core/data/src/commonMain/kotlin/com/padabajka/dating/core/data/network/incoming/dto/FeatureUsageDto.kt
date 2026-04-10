package com.padabajka.dating.core.data.network.incoming.dto

import com.padabajka.dating.core.repository.api.model.subscription.FeatureUsage
import kotlinx.serialization.Serializable

@Serializable
data class FeatureUsageDto(
    val returns: Int,
    val superLikes: Int
)

fun FeatureUsageDto.toDomain() = FeatureUsage(
    returns = returns,
    superLikes = superLikes
)

fun FeatureUsage.toDto() = FeatureUsageDto(
    returns = returns,
    superLikes = superLikes
)
