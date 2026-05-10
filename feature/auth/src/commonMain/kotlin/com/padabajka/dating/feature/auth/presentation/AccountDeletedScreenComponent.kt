package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.auth.presentation.model.AccountDeletedEvent

class AccountDeletedScreenComponent(
    context: ComponentContext,
    private val logoutUseCase: LogOutUseCase,
    private val alertService: AlertService
) : BaseComponent<EmptyState>(context, "account_deleted", EmptyState) {
    fun onEvent(event: AccountDeletedEvent) {
        when (event) {
            AccountDeletedEvent.Logout -> logout()
        }
    }

    private fun logout() = launchStep(
        action = {
            logoutUseCase()
        },
        onError = ::defaultOnError
    )

    private suspend fun defaultOnError(error: ExternalDomainError): Boolean {
        val error = when (error) {
            is ExternalDomainError.TextError -> error
            is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
        }

        alertService.showAlert { error.text.translate() }
        return error.needLog.not()
    }
}
