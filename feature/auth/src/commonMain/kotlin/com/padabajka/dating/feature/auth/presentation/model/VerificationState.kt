package com.padabajka.dating.feature.auth.presentation.model

import com.padabajka.dating.core.presentation.State

data class VerificationState(
    val resendState: ResendState
) : State

sealed interface ResendState {
    data object Available : ResendState
    data class Unavailable(val time: Int) : ResendState
}
