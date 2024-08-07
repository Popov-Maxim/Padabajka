package com.fp.padabajka.feature.swiper.data.search.model

import com.fp.padabajka.core.repository.api.model.profile.AgeRange
import com.fp.padabajka.core.repository.api.model.profile.Gender
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import kotlinx.collections.immutable.PersistentList

data class SearchPrefDto(
    val ageRange: AgeRange,
    val lookingGenders: PersistentList<Gender>,
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
