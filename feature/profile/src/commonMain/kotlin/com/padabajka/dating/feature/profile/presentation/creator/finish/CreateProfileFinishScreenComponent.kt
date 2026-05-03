package com.padabajka.dating.feature.profile.presentation.creator.finish

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.core.presentation.error.ExternalDomainError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.profile.domain.CreateProfileFromDraftUseCase

class CreateProfileFinishScreenComponent(
    context: ComponentContext,
    private val createProfileFromDraftUseCase: CreateProfileFromDraftUseCase,
    private val alertService: AlertService
) : BaseComponent<EmptyState>(
    context,
    "create_profile_finish",
    EmptyState
) {
    init {
        saveProfile()
    }

    private fun saveProfile() = launchStep(
        action = {
            createProfileFromDraftUseCase()
        },
        onError = {
            val error = when (it) {
                is ExternalDomainError.TextError -> it
                is ExternalDomainError.Unknown -> ExternalDomainError.TextError.Unknown
            }

            alertService.showAlert { error.text.translate() }
            error.needLog
        }
    )
}
