package com.padabajka.dating.core.presentation.error

import com.padabajka.dating.core.repository.api.AuthSessionHandler
import com.padabajka.dating.core.repository.api.DeeplinkHandler
import com.padabajka.dating.core.repository.api.exception.UserException
import com.padabajka.dating.core.repository.api.model.deeplink.AppDeeplink
import com.padabajka.dating.core.utils.isDebugBuild
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DomainErrorHandler(
    private val coroutineScope: CoroutineScope,
    private val deeplinkHandler: DeeplinkHandler,
    private val authSessionHandler: AuthSessionHandler,
) {

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
            is InternalDomainError.User -> handleUserException(error.error)
        }
    }

    private fun handleUserException(error: UserException) {
        coroutineScope.launch {
            when (error) {
                is UserException.Banned,
                is UserException.Deleted -> {
                    deeplinkHandler.handle(
                        AppDeeplink.OpenUserDeleteScreen(error is UserException.Banned)
                    )
                }

                is UserException.Unauthorized -> {
                    authSessionHandler.onUnauthorized()
                }
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
