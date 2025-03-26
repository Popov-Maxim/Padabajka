package com.padabajka.dating.feature.profile.presentation.model

sealed interface ProfileEvent

data object OpenEditorEvent : ProfileEvent
data object UpdateProfileEvent : ProfileEvent
data object LogoutEvent : ProfileEvent
