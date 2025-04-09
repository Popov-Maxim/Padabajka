package com.padabajka.dating.feature.swiper.presentation.model

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.model.profile.Gender

enum class GenderUI(val textId: StaticTextId) {
    Male(StaticTextId.UiId.Male),
    Female(StaticTextId.UiId.Female),
    Everyone(StaticTextId.UiId.Everyone)
}

fun GenderUI.toGender(): Gender {
    return when (this) {
        GenderUI.Male -> Gender.MALE
        GenderUI.Female -> Gender.FEMALE
        GenderUI.Everyone -> Gender.Everyone
    }
}

fun Gender.toGenderUI(): GenderUI {
    return when (this) {
        Gender.MALE -> GenderUI.Male
        Gender.FEMALE -> GenderUI.Female
        Gender.Everyone -> GenderUI.Everyone
    }
}
