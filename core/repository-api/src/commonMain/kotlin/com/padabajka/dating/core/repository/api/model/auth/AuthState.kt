package com.padabajka.dating.core.repository.api.model.auth

sealed class AuthState

data object LoggedOut : AuthState()

data class LoggedIn(val userId: UserId) : AuthState()

@Deprecated("All registrations are processed via email confirmation.")
data class WaitingForEmailValidation(val userId: UserId) : AuthState()

fun AuthState.userIdOrNull(): UserId? {
    return when (this) {
        LoggedOut -> null
        is LoggedIn -> userId
        is WaitingForEmailValidation -> userId
    }
}
