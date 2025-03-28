package com.padabajka.dating.feature.profile.presentation.editor

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.event.raisedIfNotNull
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.feature.image.domain.GetLocalImageUseCase
import com.padabajka.dating.feature.profile.domain.SaveProfileUseCase
import com.padabajka.dating.feature.profile.presentation.editor.model.AboutMeFieldLoosFocusEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.AboutMeFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ConsumeInternalErrorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DiscardProfileUpdatesClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.FirstNameFieldLoosFocusEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.FirstNameFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.HideAchievementClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ImageAddEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LastNameFieldLoosFocusEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LastNameFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.MakeAchievementMainClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.MakeAchievementVisibleClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorState
import com.padabajka.dating.feature.profile.presentation.editor.model.RemoveMainAchievementClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SaveProfileUpdatesClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.toEditorState
import com.padabajka.dating.feature.profile.presentation.editor.model.updated
import com.padabajka.dating.feature.profile.presentation.model.InternalError

class ProfileEditorScreenComponent(
    context: ComponentContext,
    private val profileRepository: ProfileRepository,
    saveProfileUseCaseFactory: Factory<SaveProfileUseCase>,
    getLocalImageUseCaseFactory: Factory<GetLocalImageUseCase>
) : BaseComponent<ProfileEditorState>(
    context,
    initProfileState(profileRepository)
) {

    private val saveProfileUseCase by saveProfileUseCaseFactory.delegate()
    private val getLocalImageUseCase by getLocalImageUseCaseFactory.delegate()

    @Suppress("CyclomaticComplexMethod")
    fun onEvent(event: ProfileEditorEvent) {
        when (event) {
            DiscardProfileUpdatesClickEvent -> discardUpdates()
            SaveProfileUpdatesClickEvent -> saveUpdates()
            is FirstNameFieldUpdateEvent -> updateFirstName(event.firstName)
            FirstNameFieldLoosFocusEvent -> trimFirstName()
            is LastNameFieldUpdateEvent -> updateLastName(event.lastName)
            LastNameFieldLoosFocusEvent -> trimLastName()
            is AboutMeFieldUpdateEvent -> updateAboutMe(event.aboutMe)
            AboutMeFieldLoosFocusEvent -> trimAboutMe()
            is HideAchievementClickEvent -> hideAchievement(event.achievement)
            is MakeAchievementVisibleClickEvent -> makeAchievementVisible(event.achievement)
            is MakeAchievementMainClickEvent -> makeAchievementMain(event.achievement)
            RemoveMainAchievementClickEvent -> makeAchievementMain(null)
            is ImageAddEvent -> addImage(event.image)
            ConsumeInternalErrorEvent -> consumeInternalError()
            // TODO: add details and images events
        }
    }

    private fun makeAchievementMain(achievement: Achievement?) {
        reduce {
            it.changeAchievementMain(achievement)
        }
    }

    private fun makeAchievementVisible(achievement: Achievement) {
        reduce {
            it.makeAchievementVisible(achievement)
        }
    }

    private fun hideAchievement(achievement: Achievement) {
        reduce {
            it.hideAchievement(achievement)
        }
    }

    private fun discardUpdates() =
        mapAndReduceException(
            action = {
                profileRepository.profileValue?.let { profile ->
                    reduce {
                        profile.toEditorState()
                    }
                }
            },
            mapper = { InternalError },
            update = { profileState, internalError ->
                profileState.copy(internalErrorStateEvent = raisedIfNotNull(internalError))
            }
        )

    private fun saveUpdates() =
        mapAndReduceException(
            action = {
                saveProfileUseCase {
                    it.updated(state.value)
                }
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )

    private fun updateFirstName(firstName: String) {
        reduce {
            it.updateFirstName(firstName)
        }
    }

    private fun trimFirstName() {
        val firstName = state.value.firstName.value
        val trimmedFirstName = firstName.trim()
        updateFirstName(trimmedFirstName)
    }

    private fun updateLastName(lastName: String) {
        reduce {
            it.updateLastName(lastName)
        }
    }

    private fun trimLastName() {
        val lastName = state.value.lastName.value
        val trimmedLastName = lastName.trim()
        updateLastName(trimmedLastName)
    }

    private fun updateAboutMe(aboutMe: String) {
        reduce {
            it.updateAboutMe(aboutMe)
        }
    }

    private fun addImage(image: Image) = mapAndReduceException(
        action = {
            val uiImage = if (image is Image.Local) {
                getLocalImageUseCase(image)
            } else {
                image
            }
            reduce {
                it.addImage(uiImage)
            }
        },
        mapper = { TODO(it.toString()) },
        update = { state, _ -> state }
    )

    private fun trimAboutMe() {
        val aboutMe = state.value.aboutMe.value
        val trimmedAboutMe = aboutMe.trim()
        updateAboutMe(trimmedAboutMe)
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
