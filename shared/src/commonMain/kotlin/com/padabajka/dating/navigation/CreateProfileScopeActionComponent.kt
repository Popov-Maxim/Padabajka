package com.padabajka.dating.navigation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.auth.domain.LogOutUseCase

internal class CreateProfileScopeActionComponent(
    context: ComponentContext,
    private val logoutUseCase: LogOutUseCase,
    private val alertService: AlertService,
) : BaseComponent<EmptyState>(context, "create_profile_scope", EmptyState) {

    fun logout() = launchStep(
        action = { logoutUseCase() },
        onError = ::handleError
    )

    private suspend fun handleError(error: ExternalDomainError): Boolean {
        val textError = when (error) {
            is ExternalDomainError.TextError -> error
            is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
        }

        alertService.showAlert { textError.text.translate() }
        return textError.needLog.not()
    }
}
