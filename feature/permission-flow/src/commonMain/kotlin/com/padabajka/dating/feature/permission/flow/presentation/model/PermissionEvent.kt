package com.padabajka.dating.feature.permission.flow.presentation.model

sealed interface PermissionEvent {
    data object Apply : PermissionEvent
    data object Skip : PermissionEvent
}
