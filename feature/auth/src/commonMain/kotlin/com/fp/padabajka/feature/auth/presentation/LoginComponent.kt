package com.fp.padabajka.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raisedWithContentIfNotNull
import com.fp.padabajka.feature.auth.domain.EmailIsBlankException
import com.fp.padabajka.feature.auth.domain.InvalidCredentialsLogInException
import com.fp.padabajka.feature.auth.domain.InvalidEmailException
import com.fp.padabajka.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.PasswordHasNoDigitsException
import com.fp.padabajka.feature.auth.domain.PasswordHasNoLowerCaseCharactersException
import com.fp.padabajka.feature.auth.domain.PasswordHasNoUpperCaseCharactersException
import com.fp.padabajka.feature.auth.domain.PasswordHasWhitespacesException
import com.fp.padabajka.feature.auth.domain.PasswordIsBlankException
import com.fp.padabajka.feature.auth.domain.PasswordIsTooShortException
import com.fp.padabajka.feature.auth.domain.UnexpectedLoginException
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.domain.ValidatePasswordsUseCase
import com.fp.padabajka.feature.auth.presentation.model.ConsumeLoginFailedEvent
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.EmailValidationIssue
import com.fp.padabajka.feature.auth.presentation.model.GoToRegistrationClick
import com.fp.padabajka.feature.auth.presentation.model.LoggingInState
import com.fp.padabajka.feature.auth.presentation.model.LoginClick
import com.fp.padabajka.feature.auth.presentation.model.LoginEvent
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.PasswordValidationIssue

// TODO Test me
@Suppress("UnusedPrivateProperty")
class LoginComponent(
    private val logInWithEmailAndPasswordUseCase: Factory<LogInWithEmailAndPasswordUseCase>,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>,
    private val validatePasswordsUseCase: Factory<ValidatePasswordsUseCase>,
    private val goToRegister: () -> Unit,
    context: ComponentContext
) :
    BaseComponent<LoggingInState>(context, LoggingInState()) {

    fun onEvent(event: LoginEvent) {
        when (event) {
            is EmailFieldUpdate -> updateEmail(event.email)
            is PasswordFieldUpdate -> updatePassword(event.password)
            EmailFieldLoosFocus -> validateEmail(state.value.email)
            PasswordFieldLoosFocus -> validatePassword(state.value.password)
            LoginClick -> login()
            ConsumeLoginFailedEvent -> consumeLoginFailedEvent()
            GoToRegistrationClick -> TODO()
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
        val trimmedPassword = password.trim()
        if (state.passwordValidationIssue != null) {
            validatePassword(trimmedPassword)
        }
        state.copy(password = trimmedPassword)
    }

    private fun login() = mapAndReduceException(
        action = {
            reduce {
                it.copy(loggingInProgress = true)
            }

            val currentState = state.value

            validateEmail(currentState.email)
            validatePassword(currentState.password)

            logInWithEmailAndPasswordUseCase.get()
                .invoke(email = currentState.email, password = currentState.password)
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
            validateEmailUseCase.get().invoke(email)
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

    private fun validatePassword(password: String) =
        mapAndReduceException(
            action = {
                validatePasswordsUseCase.get().invoke(password, password)
            },
            mapper = { exception ->
                when (exception) {
                    PasswordIsBlankException -> PasswordValidationIssue.PasswordIsBlank
                    PasswordHasWhitespacesException -> PasswordValidationIssue.PasswordHasWhitespaces
                    PasswordIsTooShortException -> PasswordValidationIssue.PasswordIsTooShort
                    PasswordHasNoLowerCaseCharactersException ->
                        PasswordValidationIssue.PasswordHasNoLowerCaseCharacters
                    PasswordHasNoUpperCaseCharactersException ->
                        PasswordValidationIssue.PasswordHasNoUpperCaseCharacters
                    PasswordHasNoDigitsException -> PasswordValidationIssue.PasswordHasNoDigits
                    else -> PasswordValidationIssue.UnableToValidatePassword
                }
            },
            update = { state, passwordValidationIssue ->
                state.copy(passwordValidationIssue = passwordValidationIssue)
            }
        )
}
