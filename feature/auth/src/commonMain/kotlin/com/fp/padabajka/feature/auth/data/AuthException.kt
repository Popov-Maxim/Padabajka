package com.fp.padabajka.feature.auth.data

sealed class AuthException(message: String) : Throwable(message)

data object InvalidCredentialsAuthException : AuthException("Invalid credentials!")
data class UnexpectedAuthException(override val cause: Throwable) :
    AuthException("Unexpected: $cause!")
