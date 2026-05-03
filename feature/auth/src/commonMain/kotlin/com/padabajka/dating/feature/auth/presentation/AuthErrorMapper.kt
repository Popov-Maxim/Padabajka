package com.padabajka.dating.feature.auth.presentation

import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError

class AuthErrorMapper {
    fun map(exception: AuthCredentialError): ExternalDomainError.TextError? {
        return when (exception) {
            is AuthCredentialError.Cancelled -> null
            is AuthCredentialError.InvalidCredentials ->
                StaticTextId.UiId.InvalidCredentialsDescription.toTextError()

            is AuthCredentialError.NetworkError ->
                ExternalDomainError.TextError.Internet

            is AuthCredentialError.NoCredentialAvailable ->
                StaticTextId.UiId.NoCredentialAvailableDescription.toTextError()

            is AuthCredentialError.TooManyRequests ->
                StaticTextId.UiId.TooManyRequestsDescription.toTextError(true)

            is AuthCredentialError.UserNotFound ->
                StaticTextId.UiId.UserNotFoundDescription.toTextError()

            is AuthCredentialError.Unknown -> ExternalDomainError.TextError.Unknown
        }
    }
}
