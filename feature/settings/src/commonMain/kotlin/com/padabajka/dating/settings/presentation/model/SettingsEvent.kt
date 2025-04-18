package com.padabajka.dating.settings.presentation.model

sealed interface SettingsEvent

data object LogOutEvent : SettingsEvent
data object NavigateBackEvent : SettingsEvent
