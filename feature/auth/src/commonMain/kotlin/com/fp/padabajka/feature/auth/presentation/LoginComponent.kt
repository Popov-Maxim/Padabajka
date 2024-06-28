package com.fp.padabajka.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.domain.delegate
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raisedWithContentIfNotNull
import com.fp.padabajka.feature.auth.domain.EmailIsBlankException
import com.fp.padabajka.feature.auth.domain.InvalidCredentialsLogInException
import com.fp.padabajka.feature.auth.domain.InvalidEmailException
import com.fp.padabajka.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.UnexpectedLoginException
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.presentation.model.ConsumeLoginFailedEvent
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.EmailValidationIssue
import com.fp.padabajka.feature.auth.presentation.model.GoToRegistrationClick
import com.fp.padabajka.feature.auth.presentation.model.LoggingInState
import com.fp.padabajka.feature.auth.presentation.model.LoginClick
import com.fp.padabajka.feature.auth.presentation.model.LoginEvent
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldUpdate

// TODO Test me
@Suppress("UnusedPrivateProperty")
class LoginComponent(
    context: ComponentContext,
    private val goToRegister: () -> Unit,
    logInWithEmailAndPasswordUseCaseFactory: Factory<LogInWithEmailAndPasswordUseCase>,
    validateEmailUseCaseFactory: Factory<ValidateEmailUseCase>,
) : BaseComponent<LoggingInState>(context, LoggingInState()) {

    private val logInWithEmailAndPasswordUseCase by logInWithEmailAndPasswordUseCaseFactory.delegate()
    private val validateEmailUseCase by validateEmailUseCaseFactory.delegate()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is EmailFieldUpdate -> updateEmail(event.email)
            is PasswordFieldUpdate -> updatePassword(event.password)
            EmailFieldLoosFocus -> validateEmail(state.value.email)
            LoginClick -> login()
            ConsumeLoginFailedEvent -> consumeLoginFailedEvent()
            GoToRegistrationClick -> goToRegister()
        }
    }

    private fun updateEmail(email: String) = reduce { state ->
        val trimmedEmail = email.trim()
        if (state.emailValidationIssue != null) {
            validateEmail(trimmedEmail)
        }
        state.copy(email = trimmedEmail)
    }

    private fun updatePassword(password: String) = reduce { state ->
        state.copy(password = password)
    }

    private fun login() = mapAndReduceException(
        action = {
            reduce {
                it.copy(loggingInProgress = true)
            }

            val currentState = state.value

            validateEmail(currentState.email)

            logInWithEmailAndPasswordUseCase(
                email = currentState.email,
                password = currentState.password
            )
        },
        mapper = { exception ->
            when (exception) {
                InvalidCredentialsLogInException -> TODO()
                // TODO Add other exceptions mapping
                is UnexpectedLoginException -> TODO()
                else -> TODO()
            }
        },
        update = { state, loginFailureReason ->
            state.copy(
                loggingInProgress = false,
                loginFailedStateEvent = raisedWithContentIfNotNull(loginFailureReason)
            )
        }
    )

    private fun consumeLoginFailedEvent() = reduce {
        it.copy(loginFailedStateEvent = consumed())
    }

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
