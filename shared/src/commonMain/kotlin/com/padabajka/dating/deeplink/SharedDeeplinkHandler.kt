package com.padabajka.dating.deeplink

import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import com.padabajka.dating.core.repository.api.exception.EmailLinkAuthException
import com.padabajka.dating.core.utils.isDebugBuild
import com.padabajka.dating.feature.auth.presentation.AuthErrorMapper
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SharedDeeplinkHandler : KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val scope: CoroutineScope by inject()
    private val deeplinkParser: DeeplinkParser by inject()
    private val alertService: AlertService by inject()
    private val authErrorMapper: AuthErrorMapper by inject()

    private val _appDeeplinks = MutableSharedFlow<AppDeeplink>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val appDeeplinks: SharedFlow<AppDeeplink> = _appDeeplinks.asSharedFlow()

    fun handle(uri: String) {
        val deeplink = deeplinkParser.parse(uri) ?: return
        scope.launch {
            when (deeplink) {
                is Deeplink.AuthLink -> runCatching {
                    authRepository.signInWithEmailLink(deeplink.link)
                }.onFailure { exception ->
                    val error = mapError(exception) ?: return@launch

                    handleError(error, exception)
                }

                is AppDeeplink -> _appDeeplinks.emit(deeplink)
            }
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
