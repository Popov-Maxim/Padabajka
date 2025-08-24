package com.padabajka.dating.feature.auth.presentation.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.event.StateEventWithContent
import com.padabajka.dating.core.presentation.event.consumed

@Immutable
data class LoggingInState(
    val email: String = "",
    val emailValidationIssue: EmailValidationIssue? = null,
    val loggingInProgress: Boolean = false,
    val loginFailedStateEvent: StateEventWithContent<LoginFailureReason> = consumed()
) : State
