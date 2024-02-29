package com.fp.padabajka.core.repository.api.model.auth

sealed class AuthState

data object LoggedOut : AuthState()

data class LoggedIn(val user: User) : AuthState()