package com.padabajka.dating.feature.legal.presentation.model

import com.padabajka.dating.core.presentation.State

data class NewLegalAgreementsState(
    val terms: String,
    val privacy: String
) : State
