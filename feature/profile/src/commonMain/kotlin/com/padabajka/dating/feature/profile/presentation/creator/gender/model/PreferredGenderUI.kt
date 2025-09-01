package com.padabajka.dating.feature.profile.presentation.creator.gender.model

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.model.profile.Gender

enum class PreferredGenderUI(val textId: StaticTextId) {
    Male(StaticTextId.UiId.Male),
    Female(StaticTextId.UiId.Female),
    Everyone(StaticTextId.UiId.Everyone)
}

fun PreferredGenderUI.toLookingGender(): List<Gender> {
    return when (this) {
        PreferredGenderUI.Male -> listOf(Gender.MALE)
        PreferredGenderUI.Female -> listOf(Gender.FEMALE)
        PreferredGenderUI.Everyone -> listOf(Gender.MALE, Gender.FEMALE)
    }
}
