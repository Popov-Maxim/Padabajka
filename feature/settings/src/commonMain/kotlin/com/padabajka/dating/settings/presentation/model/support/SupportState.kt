package com.padabajka.dating.settings.presentation.model.support

import com.padabajka.dating.core.presentation.State

data class SupportState(
    val category: SupportCategory? = null,
    val description: String = "",
    val isOpeningMail: Boolean = false,
) : State
