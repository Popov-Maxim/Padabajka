package com.padabajka.dating.feature.profile.presentation.creator.gender

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.feature.profile.domain.creator.DraftProfileProvider
import com.padabajka.dating.feature.profile.domain.update.UpdateUserGenderUseCase
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.GenderSelectorEvent
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.GenderSelectorState
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.GenderUI
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.PreferredGenderUI
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.toGender
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.toGenderUI
import com.padabajka.dating.feature.profile.presentation.creator.gender.model.toLookingGender

class CreateProfileSexScreenComponent(
    context: ComponentContext,
    draftProfileProvider: DraftProfileProvider,
    private val updateUserGenderUseCase: UpdateUserGenderUseCase,
    private val searchPreferencesRepository: SearchPreferencesRepository,
    private val toNext: () -> Unit
) : BaseComponent<GenderSelectorState>(
    context = context,
    initialState = initState(draftProfileProvider)
) {

    fun onEvent(event: GenderSelectorEvent) {
        when (event) {
            is GenderSelectorEvent.PreferredGenderSelected -> updatePreferredGender(event.gender)
            is GenderSelectorEvent.UserGenderSelected -> updateUserGender(event.gender)
            GenderSelectorEvent.PressContinue -> continueCreating()
        }
    }

    private fun updateUserGender(userGender: GenderUI) {
        reduce {
            it.copy(userGender = userGender)
        }
    }

    private fun updatePreferredGender(userGender: PreferredGenderUI) {
        reduce {
            it.copy(preferredGender = userGender)
        }
    }

    private fun continueCreating() {
        val genderUI = state.value.userGender ?: return
        val preferredGenderUI = state.value.preferredGender ?: return
        val gender = genderUI.toGender()
        val preferredGender = preferredGenderUI.toLookingGender()

        mapAndReduceException(
            action = {
                updateUserGenderUseCase(gender)
                searchPreferencesRepository.update {
                    it.copy(lookingGenders = preferredGender)
                }
                toNext()
            },
            mapper = { it },
            update = { state, _ -> state }
        )
    }

    companion object {
        private fun initState(
            draftProfileProvider: DraftProfileProvider
        ): GenderSelectorState {
            val draftProfile = draftProfileProvider.getProfile()
            val userGender = draftProfile?.gender?.toGenderUI()

            return GenderSelectorState(userGender, null)
        }
    }
}
