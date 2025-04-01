package com.padabajka.dating.core.repository.api.model.swiper

import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import com.padabajka.dating.core.repository.api.model.profile.Gender
import com.padabajka.dating.core.repository.api.model.profile.toAge
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class SearchPreferences(
    val ageRange: AgeRange,
    val lookingGenders: PersistentList<Gender>,
    val distanceInKm: Int
) {
    companion object {
        val DEFAULT: SearchPreferences = SearchPreferences(
            AgeRange(18.toAge(), 80.toAge()),
            persistentListOf(Gender.FEMALE),
            32
        )
    }
}
