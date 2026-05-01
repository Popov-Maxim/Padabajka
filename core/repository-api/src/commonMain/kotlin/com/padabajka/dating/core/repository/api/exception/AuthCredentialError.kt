package com.padabajka.dating.core.repository.api.exception

sealed class AuthCredentialError : Throwable() {

    class Cancelled : AuthCredentialError()

    class NetworkError : AuthCredentialError()

    class NoCredentialAvailable : AuthCredentialError()

    class InvalidCredentials : AuthCredentialError()

    class TooManyRequests : AuthCredentialError()

    class UserNotFound : AuthCredentialError()

    data class Unknown(override val cause: Throwable? = null) : AuthCredentialError()
}
