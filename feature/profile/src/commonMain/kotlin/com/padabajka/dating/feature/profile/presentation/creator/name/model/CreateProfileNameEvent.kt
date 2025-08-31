package com.padabajka.dating.feature.profile.presentation.creator.name.model

sealed interface CreateProfileNameEvent {
    data class NameFieldUpdate(val name: String) : CreateProfileNameEvent
    data object PressContinue : CreateProfileNameEvent
}
