package com.padabajka.dating.feature.profile.presentation.creator.lookingfor.model

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId

sealed interface CreateProfileLookingForEvent {
    data class OnDetailSelected(val detail: StaticTextId) : CreateProfileLookingForEvent
}
