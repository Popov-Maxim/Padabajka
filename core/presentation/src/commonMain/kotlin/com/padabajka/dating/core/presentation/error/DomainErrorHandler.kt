package com.padabajka.dating.core.presentation.error

import com.padabajka.dating.core.utils.isDebugBuild
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

class DomainErrorHandler {

    suspend fun handle(
        e: Throwable,
        onError: suspend (ExternalDomainError) -> Boolean = { false }
    ) {
        val mappedError = DomainError.mapError(e)
        when (mappedError) {
            is ExternalDomainError -> handleExternalError(mappedError, e, onError)
            is InternalDomainError -> handleInternalError(mappedError)
        }
    }

    private fun handleInternalError(error: InternalDomainError) {
        when (error) {
            is InternalDomainError.User -> TODO()
        }
    }

    private suspend fun handleExternalError(
        error: ExternalDomainError,
        e: Throwable,
        onError: suspend (ExternalDomainError) -> Boolean = { false }
    ) {
        val handled = onError(error)
        if (handled.not() && error.needLog) {
            if (isDebugBuild) {
                throw e
            } else {
                Firebase.crashlytics.recordException(e)
            }
        }
    }
}
