package com.fp.padabajka.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.feature.auth.domain.EmailIsBlankException
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
            is SecondPasswordFieldUpdate -> updateSecondPassword(event.password2)
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

    private fun updateSecondPassword(password2: String) = reduce<RegisteringState> { state ->
        val trimmedPassword2 = password2.trim()
        validatePasswords(password = state.password, password2 = trimmedPassword2)
        state.copy(password2 = trimmedPassword2)
    }

    private fun goToLogin() = reduce<RegisteringState> {
        LoggingInState()
    }

    private fun goToRegistering() = reduce<LoggingInState> { state ->
        RegisteringState(email = state.email)
    }

    private fun login() = TODO()

    private fun register() = TODO()

    private fun validateEmail(email: String) = mapAndReduceException(
        action = {
            validateEmailUseCase.get().invoke(email)
        }, mapper = { exception ->
            when (exception) {
                EmailIsBlankException -> EmailValidationIssue.EmailIsBlank
                InvalidEmailException -> EmailValidationIssue.EmailIsInvalid
                else -> EmailValidationIssue.UnableToValidateEmail
            }
        }, update = { state, emailValidationIssue ->
            state.abstractCopy(newEmailValidationIssue = emailValidationIssue)
        })

    private fun validatePassword(
        password: String,
        state: AuthState
    ) =
        when (state) {
            is LoggingInState -> validatePasswords(password = password, password2 = password)
            is RegisteringState -> validatePasswords(
                password = password,
                password2 = state.password2
            )
        }

    private fun validatePasswords(password: String, password2: String) = mapAndReduceException(
        action = {
            validatePasswordsUseCase.get().invoke(password, password2)
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
        }, update = { state, passwordValidationIssue ->
            state.abstractCopy(newPasswordValidationIssue = passwordValidationIssue)
        })
}
