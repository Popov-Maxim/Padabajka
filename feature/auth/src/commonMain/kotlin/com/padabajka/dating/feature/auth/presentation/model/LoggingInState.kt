package com.padabajka.dating.feature.auth.presentation.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State

@Immutable
data class LoggingInState(
    val emailFieldState: FieldState = FieldState.Editor(),
) : State {
    sealed interface FieldState {
        data class Editor(
            val email: String = "",
            val valid: Boolean = false
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
