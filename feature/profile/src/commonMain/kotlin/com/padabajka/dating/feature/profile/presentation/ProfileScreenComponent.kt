package com.padabajka.dating.feature.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.feature.auth.domain.LogOutUseCase
import com.padabajka.dating.feature.profile.presentation.model.LogoutEvent
import com.padabajka.dating.feature.profile.presentation.model.OpenEditorEvent
import com.padabajka.dating.feature.profile.presentation.model.ProfileEvent
import com.padabajka.dating.feature.profile.presentation.model.ProfileState
import com.padabajka.dating.feature.profile.presentation.model.ProfileValue
import com.padabajka.dating.feature.profile.presentation.model.UpdateProfileEvent
import com.padabajka.dating.feature.profile.presentation.model.toUIProfileValue
import kotlinx.coroutines.launch

class ProfileScreenComponent(
    context: ComponentContext,
    private val openEditor: () -> Unit,
    private val profileRepository: ProfileRepository,
    logoutUseCaseFactory: Factory<LogOutUseCase>
) : BaseComponent<ProfileState>(
    context,
    ProfileState(value = initProfileState(profileRepository))
) {

    private val logoutUseCase by logoutUseCaseFactory.delegate()

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
            LogoutEvent -> logout()
        }
    }

    private fun logout() = mapAndReduceException(
        action = {
            logoutUseCase()
        },
        mapper = { it },
        update = { state, m ->
            state
        }
    )

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
