package com.fp.padabajka.core.repository.api.model.auth

sealed class AuthException(message: String) : Throwable(message)

data object InvalidCredentialsAuthException : AuthException("Invalid credentials!")
data class UnexpectedAuthException(override val cause: Throwable) :
    AuthException("Unexpected: $cause!")
