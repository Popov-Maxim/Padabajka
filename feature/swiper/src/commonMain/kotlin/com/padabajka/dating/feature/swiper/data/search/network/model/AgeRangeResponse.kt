package com.padabajka.dating.feature.swiper.data.search.network.model

import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import com.padabajka.dating.core.repository.api.model.profile.toAge
import kotlinx.serialization.Serializable

@Serializable
data class AgeRangeResponse(
    val start: Int,
    val endInclusive: Int,
)

fun AgeRangeResponse.toDomain(): AgeRange = AgeRange(start.toAge(), endInclusive.toAge())
