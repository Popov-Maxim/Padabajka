package com.padabajka.dating.feature.auth.presentation.model

import com.padabajka.dating.core.presentation.State

data class UnauthScopeState(
    val terms: String,
    val privacy: String
) : State
