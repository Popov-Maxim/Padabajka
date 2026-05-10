package com.padabajka.dating.feature.swiper.presentation.model

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.model.profile.Gender

enum class GenderUI(val textId: StaticTextId) {
    Male(StaticTextId.UiId.Male),
    Female(StaticTextId.UiId.Female),
    Everyone(StaticTextId.UiId.Everyone)
}

fun Gender.toGenderUI(): GenderUI {
    return when (this) {
        Gender.MALE -> GenderUI.Male
        Gender.FEMALE -> GenderUI.Female
    }
}
