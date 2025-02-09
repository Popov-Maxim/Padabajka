package com.fp.padabajka.feature.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.feature.profile.presentation.model.NeedOpenEditorEvent
import com.fp.padabajka.feature.profile.presentation.model.NeedUpdateProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileState
import com.fp.padabajka.feature.profile.presentation.model.ProfileValue
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
            NeedOpenEditorEvent -> openEditor()
            NeedUpdateProfileEvent -> updateProfile()
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
        update = { state, _ ->
            state.copy(value = ProfileValue.Error)
        }
    )

    companion object {
        private fun initProfileState(profileRepository: ProfileRepository): ProfileValue {
            return profileRepository.profileValue?.toUIProfileValue() ?: ProfileValue.Loading
        }
    }
}
