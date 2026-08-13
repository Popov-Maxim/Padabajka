package com.padabajka.dating.feature.profile.presentation.creator.finish.model

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

sealed interface CreateProfileFinishState : State {
    data object Loading : CreateProfileFinishState
    data class Error(val text: StaticTextId) : CreateProfileFinishState
}
