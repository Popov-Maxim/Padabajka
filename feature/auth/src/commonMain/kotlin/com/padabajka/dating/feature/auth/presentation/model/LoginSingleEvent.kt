package com.padabajka.dating.feature.auth.presentation.model

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

sealed interface LoginSingleEvent {
    data class ShowDialog(val text: StaticTextId) : LoginSingleEvent
}
