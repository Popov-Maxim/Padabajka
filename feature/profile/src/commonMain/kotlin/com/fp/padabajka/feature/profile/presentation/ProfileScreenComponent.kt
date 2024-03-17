package com.fp.padabajka.feature.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.domain.Factory
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.core.presentation.event.consumed
import com.fp.padabajka.core.presentation.event.raisedIfNotNull
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.feature.profile.domain.DiscardUpdateUseCase
import com.fp.padabajka.feature.profile.domain.ProfileProvider
import com.fp.padabajka.feature.profile.domain.SaveUpdateProfileUseCase
import com.fp.padabajka.feature.profile.domain.update.AboutMeUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.FirstNameUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.HideAchievementUseCase
import com.fp.padabajka.feature.profile.domain.update.LastNameUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.MainAchievementUpdateUseCase
import com.fp.padabajka.feature.profile.domain.update.MakeAchievementVisibleUseCase
import com.fp.padabajka.feature.profile.presentation.model.AboutMeFieldLoosFocusEvent
import com.fp.padabajka.feature.profile.presentation.model.AboutMeFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.model.ConsumeInternalErrorEvent
import com.fp.padabajka.feature.profile.presentation.model.DiscardProfileUpdatesClickEvent
import com.fp.padabajka.feature.profile.presentation.model.FirstNameFieldLoosFocusEvent
import com.fp.padabajka.feature.profile.presentation.model.FirstNameFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.model.HideAchievementClickEvent
import com.fp.padabajka.feature.profile.presentation.model.InternalError
import com.fp.padabajka.feature.profile.presentation.model.LastNameFieldLoosFocusEvent
import com.fp.padabajka.feature.profile.presentation.model.LastNameFieldUpdateEvent
import com.fp.padabajka.feature.profile.presentation.model.MakeAchievementMainClickEvent
import com.fp.padabajka.feature.profile.presentation.model.MakeAchievementVisibleClickEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileEvent
import com.fp.padabajka.feature.profile.presentation.model.ProfileState
import com.fp.padabajka.feature.profile.presentation.model.RemoveMainAchievementClickEvent
import com.fp.padabajka.feature.profile.presentation.model.SaveProfileUpdatesClickEvent
import kotlinx.coroutines.launch

class ProfileScreenComponent(
    context: ComponentContext,
    private val profileProvider: ProfileProvider,
    private val discardUpdateUseCase: Factory<DiscardUpdateUseCase>,
    private val saveUpdateProfileUseCase: Factory<SaveUpdateProfileUseCase>,
    private val firstNameUpdateUseCase: Factory<FirstNameUpdateUseCase>,
    private val lastNameUpdateUseCase: Factory<LastNameUpdateUseCase>,
    private val aboutMeUpdateUseCase: Factory<AboutMeUpdateUseCase>,
    private val hideAchievementUseCase: Factory<HideAchievementUseCase>,
    private val makeAchievementVisibleUseCase: Factory<MakeAchievementVisibleUseCase>,
    private val mainAchievementUpdateUseCase: Factory<MainAchievementUpdateUseCase>
) : BaseComponent<ProfileState>(
    context,
    TODO("add init")
) {

    init {
        componentScope.launch {
            profileProvider.profile.collect { profile ->
                reduce { it.updated(profile) }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
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
                mainAchievementUpdateUseCase.get().invoke(achievement)
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun makeAchievementVisible(achievement: Achievement) =
        mapAndReduceException(
            action = {
                makeAchievementVisibleUseCase.get().invoke(achievement)
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun hideAchievement(achievement: Achievement) =
        mapAndReduceException(
            action = {
                hideAchievementUseCase.get().invoke(achievement)
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun discardUpdates() =
        mapAndReduceException(
            action = {
                discardUpdateUseCase.get().invoke()
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun saveUpdates() =
        mapAndReduceException(
            action = {
                saveUpdateProfileUseCase.get().invoke()
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
                firstNameUpdateUseCase.get()
                    .invoke(firstName)
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
                lastNameUpdateUseCase.get()
                    .invoke(lastName)
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
                aboutMeUpdateUseCase.get()
                    .invoke(aboutMe)
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
}
