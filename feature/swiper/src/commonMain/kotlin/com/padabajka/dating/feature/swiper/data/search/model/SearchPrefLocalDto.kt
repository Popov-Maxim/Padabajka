package com.padabajka.dating.feature.swiper.data.search.model

import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import kotlinx.serialization.Serializable

@Serializable
data class SearchPrefLocalDto(
    val ageRange: AgeRangeLocalDto,
    val lookingGenders: List<String>,
    val distanceInKm: Int
)

fun SearchPreferences.toDto(): SearchPrefLocalDto {
    return SearchPrefLocalDto(
        ageRange.toLocalDto(),
        lookingGenders.map { it.raw },
        distanceInKm
    )
}

fun SearchPrefLocalDto.toSearchPref(): SearchPreferences {
    return SearchPreferences(
        ageRange.toDomain(),
        lookingGenders.map { Gender.parse(it) },
        distanceInKm
    )
}
