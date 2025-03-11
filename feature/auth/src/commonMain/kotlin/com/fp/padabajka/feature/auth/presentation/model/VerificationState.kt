package com.fp.padabajka.feature.auth.presentation.model

import com.fp.padabajka.core.presentation.State

data class VerificationState(
    val resendState: ResendState
) : State

sealed interface ResendState {
    data object Available : ResendState
    data class Unavailable(val time: Int) : ResendState
}
