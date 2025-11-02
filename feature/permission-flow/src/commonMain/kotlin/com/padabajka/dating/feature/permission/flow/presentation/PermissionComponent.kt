package com.padabajka.dating.feature.permission.flow.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.permission.PermissionController
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.feature.permission.flow.presentation.model.PermissionEvent

class PermissionComponent(
    context: ComponentContext,
    private val permissionController: PermissionController,
    private val onApplied: () -> Unit,
    private val onSkipped: () -> Unit,
) : BaseComponent<EmptyState>(
    context,
    EmptyState
) {
    fun onEvent(event: PermissionEvent) {
        when (event) {
            PermissionEvent.Apply -> apply()
            PermissionEvent.Skip -> onSkipped()
        }
    }

    private fun apply() = mapAndReduceException(
        action = {
            val result = permissionController.requestPermission()
            if (result) {
                onApplied()
            }
        },
        mapper = {
            println("PermissionComponent: ${it.message}")
            it
        },
        update = { state, _ -> state }
    )
}
