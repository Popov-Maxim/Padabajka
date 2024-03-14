package com.fp.padabajka.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raisedWithContentIfNotNull
import com.fp.padabajka.feature.auth.domain.EmailIsBlankException
import com.fp.padabajka.feature.auth.domain.InvalidCredentialsRegisteringException
import com.fp.padabajka.feature.auth.domain.InvalidEmailException
import com.fp.padabajka.feature.auth.domain.PasswordHasNoDigitsException
import com.fp.padabajka.feature.auth.domain.PasswordHasNoLowerCaseCharactersException
import com.fp.padabajka.feature.auth.domain.PasswordHasNoUpperCaseCharactersException
import com.fp.padabajka.feature.auth.domain.PasswordHasWhitespacesException
import com.fp.padabajka.feature.auth.domain.PasswordIsBlankException
import com.fp.padabajka.feature.auth.domain.PasswordIsTooShortException
import com.fp.padabajka.feature.auth.domain.PasswordsNotMatchingException
import com.fp.padabajka.feature.auth.domain.RegisterWithEmailAndPasswordUseCase
import com.fp.padabajka.feature.auth.domain.UnexpectedRegisterException
import com.fp.padabajka.feature.auth.domain.ValidateEmailUseCase
import com.fp.padabajka.feature.auth.domain.ValidatePasswordsUseCase
import com.fp.padabajka.feature.auth.presentation.model.ConsumeRegistrationFailEvent
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.EmailFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.EmailValidationIssue
import com.fp.padabajka.feature.auth.presentation.model.GoToLoginClick
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.PasswordFieldUpdate
import com.fp.padabajka.feature.auth.presentation.model.PasswordValidationIssue
import com.fp.padabajka.feature.auth.presentation.model.RegisterClick
import com.fp.padabajka.feature.auth.presentation.model.RegisterEvent
import com.fp.padabajka.feature.auth.presentation.model.RegisteringState
import com.fp.padabajka.feature.auth.presentation.model.RegistrationFailureReason
import com.fp.padabajka.feature.auth.presentation.model.RepeatedPasswordFieldLoosFocus
import com.fp.padabajka.feature.auth.presentation.model.RepeatedPasswordFieldUpdate

// TODO Test me
@Suppress("UnusedPrivateProperty")
class RegisterComponent(
    private val registerWithEmailAndPasswordUseCase: Factory<RegisterWithEmailAndPasswordUseCase>,
    private val validateEmailUseCase: Factory<ValidateEmailUseCase>,
    private val validatePasswordsUseCase: Factory<ValidatePasswordsUseCase>,
    private val goToLogin: () -> Unit,
    context: ComponentContext
) : BaseComponent<RegisteringState>(context, RegisteringState()) {

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is EmailFieldUpdate -> updateEmail(event.email)
            is PasswordFieldUpdate -> updatePassword(event.password)
            is RepeatedPasswordFieldUpdate -> updateSecondPassword(event.repeatedPassword)
            EmailFieldLoosFocus -> validateEmail(state.value.email)
            RepeatedPasswordFieldLoosFocus, // TODO Implement proper separate passwords validation
            PasswordFieldLoosFocus -> validatePasswords(
                state.value.password,
                state.value.repeatedPassword
            )
            RegisterClick -> register()
            ConsumeRegistrationFailEvent -> consumeRegisterFailedEvent()
            GoToLoginClick -> TODO()
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
            validatePasswords(trimmedPassword, state.repeatedPassword)
        }
        state.copy(password = trimmedPassword)
    }

    private fun updateSecondPassword(repeatedPassword: String) = reduce { state ->
        val trimmedRepeatedPassword = repeatedPassword.trim()
        if (state.passwordValidationIssue != null) {
            validatePasswords(password = state.password, repeatedPassword = trimmedRepeatedPassword)
        }
        state.copy(repeatedPassword = trimmedRepeatedPassword)
    }

    private fun register() = mapAndReduceException(
        action = {
            reduce {
                it.copy(registeringInProgress = true)
            }

            val currentState = state.value

            validateEmail(currentState.email)
            validatePasswords(currentState.password, currentState.repeatedPassword)

            registerWithEmailAndPasswordUseCase.get()
                .invoke(currentState.email, currentState.password, currentState.repeatedPassword)
        },
        mapper = { exception ->
            when (exception) {
                InvalidCredentialsRegisteringException -> RegistrationFailureReason.InvalidCredentials
                // TODO Add other exceptions mapping
                is UnexpectedRegisterException -> RegistrationFailureReason.UnknownReason
                else -> TODO()
            }
        },
        update = { state, registrationFailureReason ->
            state.copy(
                registeringInProgress = false,
                registeringFailedEvent = raisedWithContentIfNotNull(registrationFailureReason)
            )
        }
    )

    private fun consumeRegisterFailedEvent() = reduce {
        it.copy(registeringFailedEvent = consumed())
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

    private fun validatePasswords(password: String, repeatedPassword: String) =
        mapAndReduceException(
            action = {
                validatePasswordsUseCase.get().invoke(password, repeatedPassword)
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
                    PasswordsNotMatchingException -> PasswordValidationIssue.PasswordsNotMatching
                    else -> PasswordValidationIssue.UnableToValidatePassword
                }
            },
            update = { state, passwordValidationIssue ->
                state.copy(passwordValidationIssue = passwordValidationIssue)
            }
        )
}
