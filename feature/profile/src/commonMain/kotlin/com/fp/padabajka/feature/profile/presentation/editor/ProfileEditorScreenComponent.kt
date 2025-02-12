package com.fp.padabajka.feature.profile.presentation.editor

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.domain.delegate
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raisedIfNotNull
import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.feature.profile.domain.DiscardUpdateUseCase
import com.fp.padabajka.feature.profile.domain.ProfileEditorProvider
import com.fp.padabajka.feature.profile.domain.SaveUpdateProfileUseCase
import com.fp.padabajka.feature.profile.domain.update.AboutMeUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.FirstNameUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.HideAchievementUseCase
import com.fp.padabajka.feature.profile.domain.update.LastNameUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.MainAchievementUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.MakeAchievementVisibleUseCase
import com.fp.padabajka.feature.profile.presentation.editor.model.AboutMeFieldLoosFocusEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.AboutMeFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.ConsumeInternalErrorEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.DiscardProfileUpdatesClickEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.FirstNameFieldLoosFocusEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.FirstNameFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.HideAchievementClickEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.LastNameFieldLoosFocusEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.LastNameFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.MakeAchievementMainClickEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.MakeAchievementVisibleClickEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.ProfileEditorState
import com.fp.padabajka.feature.profile.presentation.editor.model.RemoveMainAchievementClickEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.SaveProfileUpdatesClickEvent
import com.fp.padabajka.feature.profile.presentation.editor.model.toEditorState
import com.fp.padabajka.feature.profile.presentation.model.InternalError
import kotlinx.coroutines.launch

class ProfileEditorScreenComponent(
    context: ComponentContext,
    profileRepository: ProfileRepository,
    discardUpdateUseCaseFactory: Factory<DiscardUpdateUseCase>,
    saveUpdateProfileUseCaseFactory: Factory<SaveUpdateProfileUseCase>,
    firstNameUpdateUseCaseFactory: Factory<FirstNameUpdateUseCase>,
    lastNameUpdateUseCaseFactory: Factory<LastNameUpdateUseCase>,
    aboutMeUpdateUseCaseFactory: Factory<AboutMeUpdateUseCase>,
    hideAchievementUseCaseFactory: Factory<HideAchievementUseCase>,
    makeAchievementVisibleUseCaseFactory: Factory<MakeAchievementVisibleUseCase>,
    mainAchievementUpdateUseCaseFactory: Factory<MainAchievementUpdateUseCase>,
    private val profileEditorProvider: ProfileEditorProvider
) : BaseComponent<ProfileEditorState>(
    context,
    initProfileState(profileRepository)
) {

    private val discardUpdateUseCase by discardUpdateUseCaseFactory.delegate()
    private val saveUpdateProfileUseCase by saveUpdateProfileUseCaseFactory.delegate()
    private val firstNameUpdateUseCase by firstNameUpdateUseCaseFactory.delegate()
    private val lastNameUpdateUseCase by lastNameUpdateUseCaseFactory.delegate()
    private val aboutMeUpdateUseCase by aboutMeUpdateUseCaseFactory.delegate()
    private val hideAchievementUseCase by hideAchievementUseCaseFactory.delegate()
    private val makeAchievementVisibleUseCase by makeAchievementVisibleUseCaseFactory.delegate()
    private val mainAchievementUpdateUseCase by mainAchievementUpdateUseCaseFactory.delegate()

    init {
        componentScope.launch {
            profileEditorProvider.profile.collect { profile ->
                reduce { it.updated(profile) }
            }
        }
    }

    fun onEvent(event: ProfileEditorEvent) {
        when (event) {
            DiscardProfileUpdatesClickEvent -> discardUpdates()
            SaveProfileUpdatesClickEvent -> saveUpdates()
            is FirstNameFieldUpdateEvent -> firstNameUpdate(event.firstName)
            FirstNameFieldLoosFocusEvent -> trimFirstName()
            is LastNameFieldUpdateEvent -> lastNameUpdate(event.lastName)
            LastNameFieldLoosFocusEvent -> trimLastName()
            is AboutMeFieldUpdateEvent -> aboutMeUpdate(event.aboutMe)
            AboutMeFieldLoosFocusEvent -> trimAboutMe()
            is HideAchievementClickEvent -> hideAchievement(event.achievement)
            is MakeAchievementVisibleClickEvent -> makeAchievementVisible(event.achievement)
            is MakeAchievementMainClickEvent -> makeAchievementMain(event.achievement)
            RemoveMainAchievementClickEvent -> makeAchievementMain(null)
            ConsumeInternalErrorEvent -> consumeInternalError()
            // TODO: add details and images events
        }
    }

    private fun makeAchievementMain(achievement: Achievement?) =
        mapAndReduceException(
            action = {
                mainAchievementUpdateUseCase(achievement)
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun makeAchievementVisible(achievement: Achievement) =
        mapAndReduceException(
            action = {
                makeAchievementVisibleUseCase(achievement)
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun hideAchievement(achievement: Achievement) =
        mapAndReduceException(
            action = {
                hideAchievementUseCase(achievement)
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun discardUpdates() =
        mapAndReduceException(
            action = {
                discardUpdateUseCase()
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun saveUpdates() =
        mapAndReduceException(
            action = {
                saveUpdateProfileUseCase()
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun firstNameUpdate(firstName: String) =
        mapAndReduceException(
            action = {
                firstNameUpdateUseCase(firstName)
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun trimFirstName() {
        val firstName = state.value.firstName.value
        val trimmedFirstName = firstName.trim()
        firstNameUpdate(trimmedFirstName)
    }

    private fun lastNameUpdate(lastName: String) =
        mapAndReduceException(
            action = {
                lastNameUpdateUseCase(lastName)
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun trimLastName() {
        val lastName = state.value.lastName.value
        val trimmedLastName = lastName.trim()
        lastNameUpdate(trimmedLastName)
    }

    private fun aboutMeUpdate(aboutMe: String) =
        mapAndReduceException(
            action = {
                aboutMeUpdateUseCase(aboutMe)
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun trimAboutMe() {
        val aboutMe = state.value.aboutMe.value
        val trimmedAboutMe = aboutMe.trim()
        aboutMeUpdate(trimmedAboutMe)
    }

    private fun consumeInternalError() = reduce {
        it.copy(internalErrorStateEvent = consumed)
    }

    companion object {
        private fun initProfileState(profileRepository: ProfileRepository): ProfileEditorState {
            return profileRepository.profileValue?.toEditorState() ?: TODO()
        }
    }
}
