package com.padabajka.dating.feature.profile.presentation.creator.finish

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.feature.image.presentation.mapImageError
import com.padabajka.dating.feature.profile.domain.CreateProfileFromDraftUseCase
import com.padabajka.dating.feature.profile.presentation.creator.finish.model.CreateProfileFinishEvent
import com.padabajka.dating.feature.profile.presentation.creator.finish.model.CreateProfileFinishState
import kotlinx.coroutines.Job

class CreateProfileFinishScreenComponent(
    context: ComponentContext,
    private val createProfileFromDraftUseCase: CreateProfileFromDraftUseCase,
    private val navigateBack: () -> Unit,
) : BaseComponent<CreateProfileFinishState>(
    context,
    "create_profile_finish",
    CreateProfileFinishState.Loading
) {
    private var saveProfileJob: Job? = null

    init {
        saveProfile()
    }

    fun onEvent(event: CreateProfileFinishEvent) {
        when (event) {
            CreateProfileFinishEvent.Retry -> saveProfile()
            CreateProfileFinishEvent.Back -> navigateBack()
        }
    }

    private fun saveProfile() {
        if (saveProfileJob?.isActive == true) return

        reduce { CreateProfileFinishState.Loading }
        saveProfileJob = launchStep(
            action = {
                createProfileFromDraftUseCase()
            },
            onError = {
                val error = it.toTextError(::mapImageError)

                reduce { CreateProfileFinishState.Error(error.text) }
                error.needLog.not()
            }
        )
    }
}
