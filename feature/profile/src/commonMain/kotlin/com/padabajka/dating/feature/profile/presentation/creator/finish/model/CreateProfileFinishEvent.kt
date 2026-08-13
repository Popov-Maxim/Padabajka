package com.padabajka.dating.feature.profile.presentation.creator.finish.model

sealed interface CreateProfileFinishEvent {
    data object Retry : CreateProfileFinishEvent
    data object Back : CreateProfileFinishEvent
}
