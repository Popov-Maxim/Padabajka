package com.padabajka.dating.core.repository.api.model.auth

sealed class AuthState

data object LoggedOut : AuthState()

data class LoggedIn(val userId: UserId) : AuthState()

data class WaitingForEmailValidation(val userId: UserId) : AuthState()
