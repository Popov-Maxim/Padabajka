package com.padabajka.dating.deeplink

import com.padabajka.dating.core.presentation.deeplink.AppDeeplinkHandler
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.DeeplinkHandler
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import com.padabajka.dating.core.repository.api.exception.EmailLinkAuthException
import com.padabajka.dating.core.repository.api.model.deeplink.AppDeeplink
import com.padabajka.dating.core.repository.api.model.deeplink.Deeplink
import com.padabajka.dating.core.utils.isDebugBuild
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.auth.presentation.AuthErrorMapper
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

class DeeplinkHandlerImpl(
    private val authRepository: AuthRepository,
    private val alertService: AlertService,
    private val authErrorMapper: AuthErrorMapper,
    private val appDeeplinkHandler: AppDeeplinkHandler,
    private val logOutUseCase: LogOutUseCase,
) : DeeplinkHandler {

    override suspend fun handle(deeplink: Deeplink) {
        when (deeplink) {
            is Deeplink.AuthLink -> runCatching {
                authRepository.signInWithEmailLink(deeplink.link)
            }.onFailure { exception ->
                val error = mapError(exception) ?: return

                handleError(error, exception)
            }

            is AppDeeplink -> appDeeplinkHandler.handle(deeplink)
            Deeplink.Logout -> logOutUseCase()
        }
    }

    private fun mapError(exception: Throwable): ExternalDomainError.TextError? {
        return when (exception) {
            is EmailLinkAuthException -> {
                when (exception) {
                    is EmailLinkAuthException.InvalidLink ->
                        StaticTextId.UiId.InvalidLinkForAuthDescription.toTextError()

                    is EmailLinkAuthException.MissingEmail ->
                        StaticTextId.UiId.MissingEmailForLinkDescription.toTextError()
                }
            }

            is AuthCredentialError -> {
                authErrorMapper.map(exception) ?: return null
            }

            else -> {
                ExternalDomainError.TextError.Unknown
            }
        }
    }

    private suspend fun handleError(error: ExternalDomainError.TextError, exception: Throwable) {
        alertService.showAlert { error.text.translate() }
        if (error.needLog) {
            if (isDebugBuild) {
                throw exception
            } else {
                Firebase.crashlytics.recordException(exception)
            }
        }
    }
}
