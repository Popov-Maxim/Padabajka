package com.padabajka.dating.core.presentation.error

import com.padabajka.dating.core.repository.api.AuthSessionHandler
import com.padabajka.dating.core.repository.api.DeeplinkHandler
import com.padabajka.dating.core.repository.api.exception.UserException
import com.padabajka.dating.core.repository.api.model.deeplink.AppDeeplink
import com.padabajka.dating.core.utils.isDebugBuild
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class DomainErrorHandler(
    private val deeplinkHandler: DeeplinkHandler,
    private val authSessionHandler: AuthSessionHandler,
) {

    suspend fun handle(
        e: Throwable,
        onError: suspend (ExternalDomainError) -> Boolean = { false }
    ) {
        currentCoroutineContext().ensureActive()
        val mappedError = DomainError.mapError(e)
        when (mappedError) {
            is ExternalDomainError -> handleExternalError(mappedError, e, onError)
            is InternalDomainError -> handleInternalError(mappedError)
        }
    }

    private suspend fun handleInternalError(error: InternalDomainError) {
        when (error) {
            is InternalDomainError.User -> handleUserException(error.error)
        }
    }

    private suspend fun handleUserException(error: UserException) {
        when (error) {
            is UserException.Banned,
            is UserException.Deleted -> {
                // TODO(P2): Bind queued error navigation to its originating session.
                deeplinkHandler.handle(
                    AppDeeplink.OpenUserDeleteScreen(error is UserException.Banned)
                )
            }

            is UserException.Unauthorized -> {
                authSessionHandler.onUnauthorized()
            }
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
