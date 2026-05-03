package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import com.padabajka.dating.feature.auth.domain.method.GoogleLoginUseCase
import com.padabajka.dating.feature.auth.presentation.model.LoginMethodEvent

class LoginMethodsComponent(
    context: ComponentContext,
    private val goToEmailMethodScreen: () -> Unit,
    private val goToDebugMethodScreen: () -> Unit,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val authErrorMapper: AuthErrorMapper,
    private val alertService: AlertService
) : BaseComponent<EmptyState>(context, "login_methods", EmptyState) {

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
            val error = when (it) {
                is ExternalDomainError.TextError -> it
                is ExternalDomainError.Unknown -> {
                    val exception = it.e
                    if (exception is AuthCredentialError) {
                        authErrorMapper.map(exception) ?: return@launchStep true
                    } else {
                        ExternalDomainError.TextError.Unknown
                    }
                }
            }

            alertService.showAlert { error.text.translate() }

            error.needLog.not()
        }
    )
}
