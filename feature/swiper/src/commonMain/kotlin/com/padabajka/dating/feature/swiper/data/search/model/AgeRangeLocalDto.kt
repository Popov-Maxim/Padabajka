package com.padabajka.dating.feature.swiper.data.search.model

import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import kotlinx.serialization.Serializable

@Serializable
class AgeRangeLocalDto(
    val start: Age,
    val endInclusive: Age
)

fun AgeRangeLocalDto.toDomain(): AgeRange {
    return AgeRange(start, endInclusive)
}

fun AgeRange.toLocalDto(): AgeRangeLocalDto {
    return AgeRangeLocalDto(start, endInclusive)
}
