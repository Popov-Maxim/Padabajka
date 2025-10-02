package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.feature.auth.domain.EmailIsBlankException
import com.padabajka.dating.feature.auth.domain.InvalidEmailException
import com.padabajka.dating.feature.auth.domain.LoginEmailOnlyUseCase
import com.padabajka.dating.feature.auth.domain.OpenMailAppUseCase
import com.padabajka.dating.feature.auth.domain.ValidateEmailUseCase
import com.padabajka.dating.feature.auth.presentation.model.EmailLoginMethodEvent
import com.padabajka.dating.feature.auth.presentation.model.EmailValidationIssue
import com.padabajka.dating.feature.auth.presentation.model.LoggingInState
import com.padabajka.dating.feature.auth.presentation.model.email

class EmailLoginMethodComponent(
    context: ComponentContext,
    private val goToLoginMethodScreen: () -> Unit,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val loginEmailOnlyUseCase: LoginEmailOnlyUseCase,
    private val openMailAppUseCase: OpenMailAppUseCase
) : BaseComponent<LoggingInState>(context, LoggingInState()) {

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

    private fun openEmailApp() = mapAndReduceException(
        action = {
            openMailAppUseCase()
        },
        mapper = {
            it
        },
        update = { state, _ -> state }
    )

    private fun updateEmail(email: String) = reduce { state ->
        when (val emailFieldState = state.emailFieldState) {
            is LoggingInState.FieldState.Editor -> {
                val trimmedEmail = email.trim()
                if (emailFieldState.emailValidationIssue != null) {
                    validateEmail(trimmedEmail, emailFieldState)
                }
                state.copy(emailFieldState = emailFieldState.copy(trimmedEmail))
            }

            is LoggingInState.FieldState.WaitSignFromEmail -> TODO()
        }
    }

    private fun login() = mapAndReduceException(
        action = {
            when (val emailFieldState = state.value.emailFieldState) {
                is LoggingInState.FieldState.Editor -> {
                    val email = emailFieldState.email
                    loginEmailOnlyUseCase(email)
                    reduce { state ->
                        state.copy(
                            emailFieldState = LoggingInState.FieldState.WaitSignFromEmail(email)
                        )
                    }
                }
                is LoggingInState.FieldState.WaitSignFromEmail -> TODO()
            }
        },
        mapper = {
            println("EmailLoginMethodComponent: ${it.message}")
            it
        },
        update = { state, _ -> state }
    )

    private fun validateEmail(email: String, emailFieldState: LoggingInState.FieldState.Editor) = mapAndReduceException(
        action = {
            validateEmailUseCase(email)
        },
        mapper = { exception ->
            when (exception) {
                EmailIsBlankException -> EmailValidationIssue.EmailIsBlank
                InvalidEmailException -> EmailValidationIssue.EmailIsInvalid
                else -> EmailValidationIssue.UnableToValidateEmail
            }
        },
        update = { state, emailValidationIssue ->
            state.copy(emailFieldState = emailFieldState.copy(emailValidationIssue = emailValidationIssue))
        }
    )
}
