package com.fp.padabajka.feature.auth.presentation.model

sealed interface LoginEvent
sealed interface RegisterEvent

data class EmailFieldUpdate(val email: String) : LoginEvent, RegisterEvent
data class PasswordFieldUpdate(val password: String) : LoginEvent, RegisterEvent
data class SecondPasswordFieldUpdate(val repeatedPassword: String) : RegisterEvent
data object EmailFieldLoosFocus : LoginEvent, RegisterEvent
data object PasswordFieldLoosFocus : LoginEvent, RegisterEvent

data object LoginClick : LoginEvent
data object RegisterClick : RegisterEvent

data object ConsumeLoginFailedEvent : LoginEvent
data object ConsumeRegistrationFailEvent : RegisterEvent

data object GoToLoginClick : RegisterEvent
data object GoToRegistrationClick : LoginEvent
