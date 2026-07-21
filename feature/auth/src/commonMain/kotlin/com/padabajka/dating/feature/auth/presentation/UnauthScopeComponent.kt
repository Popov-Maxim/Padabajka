package com.padabajka.dating.feature.auth.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.model.legal.LegalVersions
import com.padabajka.dating.feature.auth.presentation.model.UnauthScopeState
import com.padabajka.dating.feature.legal.domain.LegalVersionsProvider

class UnauthScopeComponent(
    context: ComponentContext,
    private val legalVersionsProvider: LegalVersionsProvider
) : BaseComponent<UnauthScopeState>(context, "unauth_scope", LegalVersionsProvider.default.toUI()) {

    init {
        launchStep(
            action = {
                val legalVersions = legalVersionsProvider.get()
                reduce {
                    legalVersions.toUI()
                }
            }
        )
    }

    companion object {
        private fun LegalVersions.toUI(): UnauthScopeState {
            return UnauthScopeState(
                terms = terms,
                privacy = privacy
            )
        }
    }
}
