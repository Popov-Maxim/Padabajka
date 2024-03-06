package com.fp.padabajka.feature.auth.presentation.model

sealed interface AuthEvent

data class EmailFieldUpdate(val email: String) : AuthEvent
data class PasswordFieldUpdate(val password: String) : AuthEvent
data class SecondPasswordFieldUpdate(val repeatedPassword: String) : AuthEvent
data object EmailFieldLoosFocus : AuthEvent
data object PasswordFieldLoosFocus : AuthEvent

data object LoginClick : AuthEvent
data object RegisterClick : AuthEvent

data object GoToLoginClick : AuthEvent
data object GoToRegistrationClick : AuthEvent