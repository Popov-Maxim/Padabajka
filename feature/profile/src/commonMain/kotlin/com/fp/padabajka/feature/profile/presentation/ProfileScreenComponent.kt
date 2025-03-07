package com.fp.padabajka.feature.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.feature.profile.presentation.model.OpenEditorEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileState
import com.fp.padabajka.feature.profile.presentation.model.ProfileValue
import com.fp.padabajka.feature.profile.presentation.model.UpdateProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.toUIProfileValue
import kotlinx.coroutines.launch

class ProfileScreenComponent(
    context: ComponentContext,
    private val openEditor: () -> Unit,
    private val profileRepository: ProfileRepository
) : BaseComponent<ProfileState>(
    context,
    ProfileState(value = initProfileState(profileRepository))
) {

    init {
        componentScope.launch {
            profileRepository.profile.collect { profile ->
                println("ProfileScreenComponent: new profile $profile")
                reduce { state ->
                    state.copy(value = profile.toUIProfileValue())
                }
            }
        }
        updateProfile()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            OpenEditorEvent -> openEditor()
            UpdateProfileEvent -> updateProfile()
        }
    }

    private fun updateProfile() = mapAndReduceException(
        action = {
            reduce { state ->
                state.copy(value = ProfileValue.Loading)
            }
            profileRepository.updateProfile()
        },
        mapper = {
            it
        },
        update = { state, exception ->
            if (exception != null) state.copy(value = ProfileValue.Error) else state
        }
    )

    companion object {
        private fun initProfileState(profileRepository: ProfileRepository): ProfileValue {
            return profileRepository.profileValue?.toUIProfileValue() ?: ProfileValue.Loading
        }
    }
}
