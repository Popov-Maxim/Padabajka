package com.fp.padabajka.feature.auth.presentation.model

import androidx.compose.runtime.Stable
import com.fp.padabajka.core.presentation.State

@Stable
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

@Stable
data class LoggingInState(
    override val email: String = "",
    override val password: String = "",
    override val emailValidationIssue: EmailValidationIssue? = null,
    override val passwordValidationIssue: PasswordValidationIssue? = null
) : AuthState()

@Stable
data class RegisteringState(
    override val email: String = "",
    override val password: String = "",
    override val emailValidationIssue: EmailValidationIssue? = null,
    override val passwordValidationIssue: PasswordValidationIssue? = null,
    val password2: String = "",
) : AuthState()
