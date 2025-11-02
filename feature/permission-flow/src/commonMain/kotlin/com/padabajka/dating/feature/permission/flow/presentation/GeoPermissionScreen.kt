package com.padabajka.dating.feature.permission.flow.presentation

import androidx.compose.runtime.Composable
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.permission.flow.presentation.model.PermissionEvent

@Composable
fun GeoPermissionScreen(permissionComponent: PermissionComponent) {
    CommonPermissionScreen(
        message = StaticTextId.UiId.GeoPermissionScreenTitle.translate(),
        onApply = {
            permissionComponent.onEvent(PermissionEvent.Apply)
        },
        onSkip = {
            permissionComponent.onEvent(PermissionEvent.Skip)
        }
    )
}
