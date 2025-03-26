package com.padabajka.dating.feature.auth.presentation.model

sealed interface LoginEvent
sealed interface RegisterEvent

data class EmailFieldUpdate(val email: String) : LoginEvent, RegisterEvent
data class PasswordFieldUpdate(val password: String) : LoginEvent, RegisterEvent
data class RepeatedPasswordFieldUpdate(val repeatedPassword: String) : RegisterEvent
data object EmailFieldLoosFocus : LoginEvent, RegisterEvent
data object PasswordFieldLoosFocus : RegisterEvent
data object RepeatedPasswordFieldLoosFocus : RegisterEvent

data object LoginClick : LoginEvent
data object RegisterClick : RegisterEvent

data object ConsumeLoginFailedEvent : LoginEvent
data object ConsumeRegistrationFailEvent : RegisterEvent

data object GoToLoginClick : RegisterEvent
data object GoToRegistrationClick : LoginEvent
