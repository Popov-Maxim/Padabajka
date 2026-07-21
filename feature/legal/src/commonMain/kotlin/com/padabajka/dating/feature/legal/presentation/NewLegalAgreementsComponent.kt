package com.padabajka.dating.feature.legal.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.metadata.LegalRepository
import com.padabajka.dating.core.repository.api.model.legal.LegalVersions
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.legal.presentation.model.NewLegalAgreementsEvent
import com.padabajka.dating.feature.legal.presentation.model.NewLegalAgreementsState
import kotlinx.serialization.Serializable

class NewLegalAgreementsComponent(
    context: ComponentContext,
    private val legalData: Data,
    private val logoutUseCase: LogOutUseCase,
    private val alertService: AlertService,
    private val legalRepository: LegalRepository
) : BaseComponent<NewLegalAgreementsState>(
    context,
    "new_legal_agreements",
    NewLegalAgreementsState(
        terms = legalData.terms,
        privacy = legalData.privacy
    )
) {
    @Serializable
    data class Data(
        val privacy: String,
        val terms: String,
    )

    fun onEvent(event: NewLegalAgreementsEvent) {
        when (event) {
            NewLegalAgreementsEvent.Apply -> apply()
            NewLegalAgreementsEvent.Logout -> logout()
        }
    }

    private fun logout() = launchStep(
        action = {
            logoutUseCase()
        },
        onError = ::defaultOnError
    )

    private fun apply() = launchStep(
        action = {
            val versions = LegalVersions(
                privacy = legalData.privacy,
                terms = legalData.terms
            )
            legalRepository.acceptLegal(versions)
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
