package com.padabajka.dating.feature.permission.flow.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.permission.PermissionController
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.permission.flow.presentation.model.PermissionEvent

class PermissionComponent(
    context: ComponentContext,
    private val permissionController: PermissionController,
    private val alertService: AlertService,
    private val onApplied: () -> Unit,
    private val onSkipped: () -> Unit,
) : BaseComponent<EmptyState>(
    context,
    "permission",
    EmptyState
) {
    fun onEvent(event: PermissionEvent) {
        when (event) {
            PermissionEvent.Apply -> apply()
            PermissionEvent.Skip -> onSkipped()
        }
    }

    private fun apply() = launchStep(
        action = {
            val result = permissionController.requestPermission()
            if (result) {
                onApplied()
            }
        },
        onError = {
            val error = when (it) {
                is ExternalDomainError.TextError -> it
                is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
            }

            alertService.showAlert { error.text.translate() }
            error.needLog.not()
        }
    )
}
