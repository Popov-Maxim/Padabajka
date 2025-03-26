package com.padabajka.dating.feature.auth.presentation.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEventWithContent
import com.padabajka.dating.core.presentation.event.consumed

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
