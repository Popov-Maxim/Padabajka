package com.padabajka.dating.settings.presentation.support

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.config.SupportConfig
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.system.intent.SystemNavigator
import com.padabajka.dating.settings.presentation.model.support.SupportEvent
import com.padabajka.dating.settings.presentation.model.support.SupportState

class SupportScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val systemNavigator: SystemNavigator,
    private val alertService: AlertService,
) : BaseComponent<SupportState>(context, "support", SupportState()) {

    fun onEvent(event: SupportEvent) {
        when (event) {
            SupportEvent.NavigateBack -> navigateBack()
            is SupportEvent.SelectCategory -> reduce { it.copy(category = event.category) }
            is SupportEvent.UpdateDescription -> reduce { it.copy(description = event.description) }
            SupportEvent.OpenMail -> openMail()
        }
    }

    private fun openMail() {
        val currentState = state.value
        val category = currentState.category ?: return
        if (currentState.description.isBlank() || currentState.isOpeningMail) return

        reduce { it.copy(isOpeningMail = true) }
        launchStep(
            action = {
                try {
                    val opened = systemNavigator.composeMail(
                        address = SupportConfig.SUPPORT_EMAIL,
                        subject = "[Padabajka][${category.tag}]",
                        body = currentState.description.trim()
                    )
                    if (!opened) {
                        alertService.showAlert { StaticTextId.UiId.SupportMailUnavailable.translate() }
                    }
                } finally {
                    reduce { it.copy(isOpeningMail = false) }
                }
            },
            onError = {
                val error = it.toTextError()
                alertService.showAlert { error.text.translate() }
                error.needLog.not()
            }
        )
    }
}
