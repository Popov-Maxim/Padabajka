package com.padabajka.dating.settings.presentation.model

sealed interface SettingsEvent

data object LogOutEvent : SettingsEvent
data object NavigateBackEvent : SettingsEvent
data object SendPushToken : SettingsEvent
data object SyncData : SettingsEvent
data object RequestPermissionEvent : SettingsEvent
data object OpenLanguageSelectorEvent : SettingsEvent
data object DeleteAccountEvent : SettingsEvent
data object FreezeAccountEvent : SettingsEvent
