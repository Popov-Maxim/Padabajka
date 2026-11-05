package com.padabajka.dating.feature.swiper.data.search.network.model

import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import kotlinx.serialization.Serializable

@Serializable
data class SearchPreferencesResponse(
    val ageRange: AgeRangeResponse,
    val lookingGenders: List<String>,
    val distanceInKm: Int,
)

fun SearchPreferencesResponse.toDomain(): SearchPreferences = SearchPreferences(
    ageRange = ageRange.toDomain(),
    lookingGenders = lookingGenders.map { Gender.parse(it) },
    distanceInKm = distanceInKm,
)
