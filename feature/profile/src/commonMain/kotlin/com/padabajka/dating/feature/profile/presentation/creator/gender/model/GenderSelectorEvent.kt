package com.padabajka.dating.feature.profile.presentation.creator.gender.model

sealed interface GenderSelectorEvent {
    data object PressContinue : GenderSelectorEvent
    data class UserGenderSelected(val gender: GenderUI) : GenderSelectorEvent
    data class PreferredGenderSelected(val gender: PreferredGenderUI) : GenderSelectorEvent
}
