package com.fp.padabajka.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.raisedIfNotNull
import com.fp.padabajka.feature.auth.domain.EmailIsBlankException
import com.fp.padabajka.feature.auth.domain.InvalidCredentialsException
import com.fp.padabajka.feature.auth.domain.InvalidEmailException
import com.fp.padabajka.feature.auth.domain.LogInWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.PasswordHasNoDigitsException
import com.fp.padabajka.feature.auth.domain.PasswordHasNoLowerCaseCharactersException
import com.fp.padabajka.feature.auth.domain.PasswordHasNoUpperCaseCharactersException
import com.fp.padabajka.feature.auth.domain.PasswordHasWhitespacesException
import com.fp.padabajka.feature.auth.domain.PasswordIsBlankException
import com.fp.padabajka.feature.auth.domain.PasswordIsTooShortException
import com.fp.padabajka.feature.auth.domain.PasswordsNotMatchingException
import com.fp.padabajka.feature.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.UnexpectedLoginException
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.domain.ValidatePasswordsUseCase
import com.fp.padabajka.feature.auth.presentation.model.AuthEvent
import com.fp.padabajka.feature.auth.presentation.model.AuthState
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.EmailValidationIssue
import com.fp.padabajka.feature.auth.presentation.model.GoToLoginClick
import com.fp.padabajka.feature.auth.presentation.model.GoToRegistrationClick
import com.fp.padabajka.feature.auth.presentation.model.LoggingInState
import com.fp.padabajka.feature.auth.presentation.model.LoginClick
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.PasswordValidationIssue
import com.fp.padabajka.feature.auth.presentation.model.RegisterClick
import com.fp.padabajka.feature.auth.presentation.model.RegisteringState
import com.fp.padabajka.feature.auth.presentation.model.SecondPasswordFieldUpdate

class AuthComponent(
    private val logInWithEmailAndPasswordUseCase: Factory<LogInWithEmailAndPasswordUseCase>,
    private val registerWithEmailAndPasswordUseCase: Factory<RegisterWithEmailAndPasswordUseCase>,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>,
    private val validatePasswordsUseCase: Factory<ValidatePasswordsUseCase>,
    context: ComponentContext
) :
    BaseComponent<AuthState>(context, LoggingInState()) {

    fun onEvent(event: AuthEvent) {
        when (event) {
            is EmailFieldUpdate -> updateEmail(event.email)
            is PasswordFieldUpdate -> updatePassword(event.password)
            is SecondPasswordFieldUpdate -> updateSecondPassword(event.repeatedPassword)
            EmailFieldLoosFocus -> validateEmail(state.value.email)
            PasswordFieldLoosFocus -> validatePassword(state.value.password, state.value)
            LoginClick -> login()
            RegisterClick -> register()
            GoToLoginClick -> goToLogin()
            GoToRegistrationClick -> goToRegistering()
        }
    }

    private fun updateEmail(email: String) = reduce { state ->
        val trimmedEmail = email.trim()
        if (state.emailValidationIssue != null) {
            validateEmail(trimmedEmail)
        }
        state.abstractCopy(newEmail = trimmedEmail)
    }

    private fun updatePassword(password: String) = reduce { state ->
        val trimmedPassword = password.trim()
        if (state.passwordValidationIssue != null) {
            validatePassword(trimmedPassword, state)
        }
        state.abstractCopy(newPassword = trimmedPassword)
    }

    private fun updateSecondPassword(repeatedPassword: String) =
        reduceChecked<RegisteringState> { state ->
            val trimmedRepeatedPassword = repeatedPassword.trim()
            validatePasswords(password = state.password, repeatedPassword = trimmedRepeatedPassword)
            state.copy(repeatedPassword = trimmedRepeatedPassword)
        }

    private fun goToLogin() = reduceChecked<RegisteringState> {
        LoggingInState()
    }

    private fun goToRegistering() = reduceChecked<LoggingInState> { state ->
        RegisteringState(email = state.email)
    }

    private fun login() = mapAndReduceException(
        action = {
            reduceChecked<LoggingInState> {
                it.copy(loggingInProgress = true)
            }

            val currentState = state.value

            validateEmail(currentState.email)
            validatePassword(currentState.password, currentState)

            logInWithEmailAndPasswordUseCase.get()
                .invoke(email = currentState.email, password = currentState.password)
        }, mapper = { exception ->
            when (exception) {
                InvalidCredentialsException -> TODO()
                // TODO Add other exceptions mapping
                is UnexpectedLoginException -> TODO()
                else -> TODO()
            }
        }, update = { state: LoggingInState, loginFailureReason ->
            state.copy(
                loggingInProgress = false,
                loginFailedStateEvent = raisedIfNotNull(loginFailureReason)
            )
        }
    )

    private fun register() = mapAndReduceException(
        action = {
            reduceChecked<RegisteringState> {
                it.copy(registeringInProgress = true)
            }

            //TODO cover with debug check
            val currentState = state.value as RegisteringState

            validateEmail(currentState.email)
            validatePasswords(currentState.password, currentState.re)
        }, mapper = {
            TODO()
        }, update = { state: RegisteringState, registrationFilureReason ->
            TODO()
        }
    )

    private fun validateEmail(email: String) = mapAndReduceException(
        action = {
            validateEmailUseCase.get().invoke(email)
        }, mapper = { exception ->
            when (exception) {
                EmailIsBlankException -> EmailValidationIssue.EmailIsBlank
                InvalidEmailException -> EmailValidationIssue.EmailIsInvalid
                else -> EmailValidationIssue.UnableToValidateEmail
            }
        }, update = { state: AuthState, emailValidationIssue ->
            state.abstractCopy(newEmailValidationIssue = emailValidationIssue)
        })

    private fun validatePassword(
        password: String,
        state: AuthState
    ) =
        when (state) {
            is LoggingInState -> validatePasswords(password = password, repeatedPassword = password)
            is RegisteringState -> validatePasswords(
                password = password,
                repeatedPassword = state.repeatedPassword
            )
        }

    private fun validatePasswords(password: String, repeatedPassword: String) =
        mapAndReduceException(
            action = {
                validatePasswordsUseCase.get().invoke(password, repeatedPassword)
            }, mapper = { exception ->
                when (exception) {
                    PasswordIsBlankException -> PasswordValidationIssue.PasswordIsBlank
                    PasswordHasWhitespacesException -> PasswordValidationIssue.PasswordHasWhitespaces
                    PasswordIsTooShortException -> PasswordValidationIssue.PasswordIsTooShort
                    PasswordHasNoLowerCaseCharactersException -> PasswordValidationIssue.PasswordHasNoLowerCaseCharacters
                    PasswordHasNoUpperCaseCharactersException -> PasswordValidationIssue.PasswordHasNoUpperCaseCharacters
                    PasswordHasNoDigitsException -> PasswordValidationIssue.PasswordHasNoDigits
                    PasswordsNotMatchingException -> PasswordValidationIssue.PasswordsNotMatching
                    else -> PasswordValidationIssue.UnableToValidatePassword
                }
            }, update = { state: AuthState, passwordValidationIssue ->
                state.abstractCopy(newPasswordValidationIssue = passwordValidationIssue)
            })
}
