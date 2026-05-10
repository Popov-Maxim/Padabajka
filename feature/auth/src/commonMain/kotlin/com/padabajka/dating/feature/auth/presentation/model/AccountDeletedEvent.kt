package com.padabajka.dating.feature.auth.presentation.model

sealed interface AccountDeletedEvent {
    data object Logout : AccountDeletedEvent
}
