package com.padabajka.dating.feature.swiper.data.search.model

import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences

data class SearchPrefDto(
    val ageRange: AgeRange,
    val lookingGenders: List<Gender>,
    val distanceInKm: Int
)

fun SearchPreferences.toDto(): SearchPrefDto {
    return SearchPrefDto(
        ageRange,
        lookingGenders,
        distanceInKm
    )
}

fun SearchPrefDto.toSearchPref(): SearchPreferences {
    return SearchPreferences(
        ageRange,
        lookingGenders,
        distanceInKm
    )
}
