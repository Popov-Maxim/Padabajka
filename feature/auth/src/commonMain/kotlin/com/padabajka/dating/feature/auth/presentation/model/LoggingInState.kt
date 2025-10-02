package com.padabajka.dating.feature.auth.presentation.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEventWithContent
import com.padabajka.dating.core.presentation.event.consumed

@Immutable
data class LoggingInState(
    val emailFieldState: FieldState = FieldState.Editor(),
    val loginFailedStateEvent: StateEventWithContent<LoginFailureReason> = consumed()
) : State {
    sealed interface FieldState {
        data class Editor(
            val email: String = "",
            val emailValidationIssue: EmailValidationIssue? = null,
        ) : FieldState

        data class WaitSignFromEmail(
            val email: String
        ) : FieldState
    }
}

fun LoggingInState.FieldState.email() = when (this) {
    is LoggingInState.FieldState.Editor -> email
    is LoggingInState.FieldState.WaitSignFromEmail -> email
}
