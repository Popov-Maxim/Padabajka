package com.fp.padabajka.feature.auth.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.presentation.event.StateEventWithContent
import com.fp.padabajka.core.presentation.event.consumed

@Immutable
data class RegisteringState(
    val email: String = "",
    val password: String = "",
    val emailValidationIssue: EmailValidationIssue? = null,
    val passwordValidationIssue: PasswordValidationIssue? = null,
    val repeatedPassword: String = "",
    val registeringInProgress: Boolean = false,
    val registeringFailedEvent: StateEventWithContent<RegistrationFailureReason> = consumed()
) : State
