package com.padabajka.dating.feature.profile.presentation.creator.finish

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.EmptyState
import com.padabajka.dating.feature.profile.domain.CreateProfileFromDraftUseCase

class CreateProfileFinishScreenComponent(
    context: ComponentContext,
    private val createProfileFromDraftUseCase: CreateProfileFromDraftUseCase
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
    )
}
