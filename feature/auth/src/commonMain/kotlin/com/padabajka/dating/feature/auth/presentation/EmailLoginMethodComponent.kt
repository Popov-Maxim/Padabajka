package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.exception.AuthCredentialError
import com.padabajka.dating.feature.auth.domain.LoginEmailOnlyUseCase
import com.padabajka.dating.feature.auth.domain.OpenMailAppUseCase
import com.padabajka.dating.feature.auth.domain.ValidateEmailUseCase
import com.padabajka.dating.feature.auth.presentation.model.EmailLoginMethodEvent
import com.padabajka.dating.feature.auth.presentation.model.LoggingInState
import com.padabajka.dating.feature.auth.presentation.model.email
import kotlinx.coroutines.Job

class EmailLoginMethodComponent(
    context: ComponentContext,
    private val goToLoginMethodScreen: () -> Unit,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val loginEmailOnlyUseCase: LoginEmailOnlyUseCase,
    private val openMailAppUseCase: OpenMailAppUseCase,
    private val authErrorMapper: AuthErrorMapper,
    private val alertService: AlertService
) : BaseComponent<LoggingInState>(context, "email_login_method", LoggingInState()) {

    private var sendLinkJob: Job? = null

    fun onEvent(event: EmailLoginMethodEvent) {
        when (event) {
            EmailLoginMethodEvent.BackToLoginMethods -> goToLoginMethodScreen()
            is EmailLoginMethodEvent.EmailChanged -> updateEmail(event.email)
            EmailLoginMethodEvent.LoginClick -> login()
            EmailLoginMethodEvent.ChangeEmail -> changeEmail()
            EmailLoginMethodEvent.OpenEmailApp -> openEmailApp()
        }
    }

    private fun changeEmail() = reduce { state ->
        state.copy(emailFieldState = LoggingInState.FieldState.Editor(state.emailFieldState.email()))
    }

    private fun openEmailApp() = launchStep(
        action = {
            openMailAppUseCase()
        }
    )

    private fun updateEmail(email: String) = reduce { state ->
        when (val emailFieldState = state.emailFieldState) {
            is LoggingInState.FieldState.Editor -> {
                val trimmedEmail = email.trim()
                val emailValid =
                    runCatching { validateEmailUseCase.invoke(trimmedEmail) }.isSuccess
                state.copy(
                    emailFieldState = emailFieldState.copy(
                        email = trimmedEmail,
                        valid = emailValid
                    )
                )
            }

            is LoggingInState.FieldState.WaitSignFromEmail -> state
        }
    }

    private fun login() {
        if (sendLinkJob?.isActive == true) return

        val emailFieldState = state.value.emailFieldState
        if (emailFieldState is LoggingInState.FieldState.Editor && emailFieldState.valid.not()) return
        val email = emailFieldState.email()

        sendLinkJob = launchStep(
            action = {
                loginEmailOnlyUseCase(email)
                if (emailFieldState is LoggingInState.FieldState.Editor) {
                    reduce { state ->
                        if (state.emailFieldState == emailFieldState) {
                            state.copy(
                                emailFieldState = LoggingInState.FieldState.WaitSignFromEmail(email)
                            )
                        } else {
                            state
                        }
                    }
                }
            },
            onError = ::defaultOnError
        )
    }

    private suspend fun defaultOnError(error: ExternalDomainError): Boolean {
        val error = when (error) {
            is ExternalDomainError.TextError -> error
            is ExternalDomainError.Unknown -> {
                val exception = error.e
                if (exception is AuthCredentialError) {
                    authErrorMapper.map(exception) ?: return true
                } else {
                    ExternalDomainError.TextError.Unknown
                }
            }
        }

        alertService.showAlert { error.text.translate() }
        return error.needLog.not()
    }
}
