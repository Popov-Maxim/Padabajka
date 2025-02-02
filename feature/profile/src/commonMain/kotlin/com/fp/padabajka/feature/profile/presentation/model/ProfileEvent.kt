package com.fp.padabajka.feature.profile.presentation.model

sealed interface ProfileEvent

data object OpenEditorEvent : ProfileEvent
data object UpdateProfileEvent : ProfileEvent
