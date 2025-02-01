package com.fp.padabajka.feature.profile.presentation.model

sealed interface ProfileEvent

data object NeedOpenEditorEvent : ProfileEvent
data object NeedUpdateProfileEvent : ProfileEvent
