package com.padabajka.dating.feature.auth.presentation.model

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.presentation.State

@Immutable
data class DebugLoggingInState(
    val uuid: String = "",
) : State
