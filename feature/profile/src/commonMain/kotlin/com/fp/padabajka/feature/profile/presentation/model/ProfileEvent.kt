package com.fp.padabajka.feature.profile.presentation.model

sealed interface ProfileEvent

data object SaveUpdateProfileEvent : ProfileEvent
data object DiscardUpdateProfileEvent : ProfileEvent

data class FirstNameUpdateEvent(val firstName: String) : ProfileEvent
data class LastNameUpdateEvent(val lastName: String) : ProfileEvent
data class AboutMeUpdateEvent(val aboutMe: String) : ProfileEvent
