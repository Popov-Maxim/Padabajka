package com.fp.padabajka.feature.auth.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.presentation.event.StateEventWithContent
import com.fp.padabajka.core.presentation.event.consumed

@Immutable
sealed class AuthState : State {
    abstract val email: String
    abstract val password: String
    abstract val emailValidationIssue: EmailValidationIssue?
    abstract val passwordValidationIssue: PasswordValidationIssue?

    fun abstractCopy(
        newEmail: String = email,
        newPassword: String = password,
        newEmailValidationIssue: EmailValidationIssue? = emailValidationIssue,
        newPasswordValidationIssue: PasswordValidationIssue? = passwordValidationIssue
    ): AuthState = when (this) {
        is LoggingInState -> copy(
            email = newEmail,
            password = newPassword,
            emailValidationIssue = newEmailValidationIssue,
            passwordValidationIssue = newPasswordValidationIssue
        )

        is RegisteringState -> copy(
            email = newEmail,
            password = newPassword,
            emailValidationIssue = newEmailValidationIssue,
            passwordValidationIssue = newPasswordValidationIssue
        )
    }
}

@Immutable
data class LoggingInState(
    override val email: String = "",
    override val password: String = "",
    override val emailValidationIssue: EmailValidationIssue? = null,
    override val passwordValidationIssue: PasswordValidationIssue? = null,
    val loggingInProgress: Boolean = false,
    val loginFailedStateEvent: StateEventWithContent<LoginFailureReason> = consumed()
) : AuthState()

@Immutable
data class RegisteringState(
    override val email: String = "",
    override val password: String = "",
    override val emailValidationIssue: EmailValidationIssue? = null,
    override val passwordValidationIssue: PasswordValidationIssue? = null,
    val repeatedPassword: String = "",
    val registeringInProgress: Boolean = false
) : AuthState()
