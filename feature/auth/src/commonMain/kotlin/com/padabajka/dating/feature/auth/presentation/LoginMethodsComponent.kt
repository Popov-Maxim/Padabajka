package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.core.presentation.ExternalDomainError
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import com.padabajka.dating.feature.auth.domain.method.GoogleLoginUseCase
import com.padabajka.dating.feature.auth.presentation.model.LoginMethodEvent
import com.padabajka.dating.feature.auth.presentation.model.LoginSingleEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LoginMethodsComponent(
    context: ComponentContext,
    private val goToEmailMethodScreen: () -> Unit,
    private val goToDebugMethodScreen: () -> Unit,
    private val googleLoginUseCase: GoogleLoginUseCase,
) : BaseComponent<EmptyState>(context, "login_methods", EmptyState) {

    private val _event = MutableSharedFlow<LoginSingleEvent>()
    val event: Flow<LoginSingleEvent> = _event.asSharedFlow()

    fun onEvent(event: LoginMethodEvent) {
        when (event) {
            LoginMethodEvent.SelectEmailMethod -> goToEmailMethodScreen()
            LoginMethodEvent.SelectGoogleMethod -> loginGoogle()
            LoginMethodEvent.SelectDebugMethod -> goToDebugMethodScreen()
        }
    }

    private fun loginGoogle() = launchStep(
        action = {
            googleLoginUseCase.login()
        },
        onError = {
            val a = when (it) {
                is ExternalDomainError.TextError -> it
                is ExternalDomainError.Unknown -> {
                    val exception = it.e
                    if (exception is AuthCredentialError) {
                        when (exception) {
                            is AuthCredentialError.Cancelled ->
                                return@launchStep true
                            is AuthCredentialError.InvalidCredentials ->
                                StaticTextId.UiId.InvalidCredentialsDescription.toTextError()

                            is AuthCredentialError.NetworkError ->
                                ExternalDomainError.TextError.Internet

                            is AuthCredentialError.NoCredentialAvailable ->
                                StaticTextId.UiId.NoCredentialAvailableDescription.toTextError()

                            is AuthCredentialError.TooManyRequests ->
                                StaticTextId.UiId.TooManyRequestsDescription.toTextError()

                            is AuthCredentialError.UserNotFound ->
                                StaticTextId.UiId.UserNotFoundDescription.toTextError()

                            is AuthCredentialError.Unknown -> ExternalDomainError.TextError.Unknown
                        }
                    } else {
                        ExternalDomainError.TextError.Unknown
                    }
                }
            }

            _event.emit(
                LoginSingleEvent.ShowDialog(a.text)
            )

            a.needLog.not()
        }
    )

    private fun StaticTextId.UiId.toTextError(): ExternalDomainError.TextError {
        return ExternalDomainError.TextError(this, false)
    }
}
