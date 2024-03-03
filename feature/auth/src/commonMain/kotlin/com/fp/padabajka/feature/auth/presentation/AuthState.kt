package com.fp.padabajka.feature.auth.presentation

import com.fp.padabajka.core.presentation.State

sealed class AuthState : State

data class LoggingIn(
    val email: String = "",
    val password: String = ""
) : AuthState()

data class Registering(
    val email: String = "",
    val password: String = ""
) : AuthState()
