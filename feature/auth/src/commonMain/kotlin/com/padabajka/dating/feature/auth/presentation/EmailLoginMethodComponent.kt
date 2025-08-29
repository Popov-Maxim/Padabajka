package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.feature.auth.domain.EmailIsBlankException
import com.padabajka.dating.feature.auth.domain.InvalidEmailException
import com.padabajka.dating.feature.auth.domain.LoginEmailOnlyUseCase
import com.padabajka.dating.feature.auth.domain.ValidateEmailUseCase
import com.padabajka.dating.feature.auth.presentation.model.EmailLoginMethodEvent
import com.padabajka.dating.feature.auth.presentation.model.EmailValidationIssue
import com.padabajka.dating.feature.auth.presentation.model.LoggingInState

class EmailLoginMethodComponent(
    context: ComponentContext,
    private val goToLoginMethodScreen: () -> Unit,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val loginEmailOnlyUseCase: LoginEmailOnlyUseCase
) : BaseComponent<LoggingInState>(context, LoggingInState()) {

    fun onEvent(event: EmailLoginMethodEvent) {
        when (event) {
            EmailLoginMethodEvent.BackToLoginMethods -> goToLoginMethodScreen()
            is EmailLoginMethodEvent.EmailChanged -> updateEmail(event.email)
            EmailLoginMethodEvent.LoginClick -> login()
        }
    }

    private fun updateEmail(email: String) = reduce { state ->
        val trimmedEmail = email.trim()
        if (state.emailValidationIssue != null) {
            validateEmail(trimmedEmail)
        }
        state.copy(email = trimmedEmail)
    }

    private fun login() = mapAndReduceException(
        action = {
            loginEmailOnlyUseCase(state.value.email)
        },
        mapper = {
            println("EmailLoginMethodComponent: ${it.message}")
            it
        },
        update = { state, _ -> state }
    )

    private fun validateEmail(email: String) = mapAndReduceException(
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
            state.copy(emailValidationIssue = emailValidationIssue)
        }
    )
}
