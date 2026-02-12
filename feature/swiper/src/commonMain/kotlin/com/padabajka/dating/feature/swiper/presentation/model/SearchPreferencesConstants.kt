package com.padabajka.dating.feature.swiper.presentation.model

import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.rangeTo

object SearchPreferencesConstants {
    const val MAX_DISTANCE = 100
    val maxDistanceRange = 0..MAX_DISTANCE

    val maxAgeRange = Age.minAge..Age.maxAge
}
