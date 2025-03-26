package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.event.raisedWithContentIfNotNull
import com.padabajka.dating.feature.auth.domain.EmailIsBlankException
import com.padabajka.dating.feature.auth.domain.InvalidCredentialsLogInException
import com.padabajka.dating.feature.auth.domain.InvalidEmailException
import com.padabajka.dating.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.padabajka.dating.feature.auth.domain.UnexpectedLoginException
import com.padabajka.dating.feature.auth.domain.ValidateEmailUseCase
import com.padabajka.dating.feature.auth.presentation.model.ConsumeLoginFailedEvent
import com.padabajka.dating.feature.auth.presentation.model.EmailFieldLoosFocus
import com.padabajka.dating.feature.auth.presentation.model.EmailFieldUpdate
import com.padabajka.dating.feature.auth.presentation.model.EmailValidationIssue
import com.padabajka.dating.feature.auth.presentation.model.GoToRegistrationClick
import com.padabajka.dating.feature.auth.presentation.model.LoggingInState
import com.padabajka.dating.feature.auth.presentation.model.LoginClick
import com.padabajka.dating.feature.auth.presentation.model.LoginEvent
import com.padabajka.dating.feature.auth.presentation.model.PasswordFieldUpdate

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
            println("LOG: ${exception.message}")
            when (exception) {
                InvalidCredentialsLogInException -> TODO()
                // TODO Add other exceptions mapping
                is UnexpectedLoginException -> TODO()
                else -> TODO(exception.toString())
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
