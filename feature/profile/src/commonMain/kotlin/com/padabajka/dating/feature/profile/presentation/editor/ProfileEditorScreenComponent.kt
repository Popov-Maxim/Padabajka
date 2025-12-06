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
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.image.domain.GetLocalImageUseCase
import com.padabajka.dating.feature.profile.domain.SaveUpdatedProfileUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindCitiesUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindLanguageAssetsUseCase
import com.padabajka.dating.feature.profile.presentation.editor.model.AboutMeFieldLoosFocusEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.AboutMeFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.CitySearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ConsumeInternalErrorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DeleteImageEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DetailUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.DiscardProfileUpdatesClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.FoundedAssets
import com.padabajka.dating.feature.profile.presentation.editor.model.HideAchievementClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ImageAddEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LangSearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguageAssetsField
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesAssetType
import com.padabajka.dating.feature.profile.presentation.editor.model.LanguagesUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LifeStyleUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.LookingForUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.MakeAchievementMainClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.MakeAchievementVisibleClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.NameFieldLoosFocusEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.NameFieldUpdateEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.NavigateBackEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileEditorState
import com.padabajka.dating.feature.profile.presentation.editor.model.RemoveMainAchievementClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SaveProfileUpdatesClickEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedDetails
import com.padabajka.dating.feature.profile.presentation.editor.model.SupportedLifestyles
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateCitySearchEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateLangSearchEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.toEditorState
import com.padabajka.dating.feature.profile.presentation.editor.model.updated
import com.padabajka.dating.feature.profile.presentation.model.InternalError
import kotlinx.collections.immutable.toPersistentList

class ProfileEditorScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val profileRepository: ProfileRepository,
    saveUpdatedProfileUseCaseFactory: Factory<SaveUpdatedProfileUseCase>,
    getLocalImageUseCaseFactory: Factory<GetLocalImageUseCase>,
    private val findCitiesUseCase: FindCitiesUseCase,
    private val findLanguageAssetsUseCase: FindLanguageAssetsUseCase
) : BaseComponent<ProfileEditorState>(
    context,
    initProfileState(profileRepository)
) {

    private val saveProfileUseCase by saveUpdatedProfileUseCaseFactory.delegate()
    private val getLocalImageUseCase by getLocalImageUseCaseFactory.delegate()

    @Suppress("CyclomaticComplexMethod")
    fun onEvent(event: ProfileEditorEvent) {
        when (event) {
            DiscardProfileUpdatesClickEvent -> discardUpdates()
            SaveProfileUpdatesClickEvent -> saveUpdates()
            is NameFieldUpdateEvent -> updateName(event.name)
            NameFieldLoosFocusEvent -> trimName()
            is AboutMeFieldUpdateEvent -> updateAboutMe(event.aboutMe)
            AboutMeFieldLoosFocusEvent -> trimAboutMe()
            is HideAchievementClickEvent -> hideAchievement(event.achievement)
            is MakeAchievementVisibleClickEvent -> makeAchievementVisible(event.achievement)
            is MakeAchievementMainClickEvent -> makeAchievementMain(event.achievement)
            RemoveMainAchievementClickEvent -> makeAchievementMain(null)
            is ImageAddEvent -> addImage(event.image)
            ConsumeInternalErrorEvent -> consumeInternalError()
            // TODO: add details and images events
            NavigateBackEvent -> navigateBack()
            is DeleteImageEvent -> deleteImage(event.image)
            is LookingForUpdateEvent -> updateLoockingForData(event.data)
            is DetailUpdateEvent -> updateDetails(event.supportedDetails)
            is LifeStyleUpdateEvent -> updateLifestyle(event.lifestyle)
            is CitySearchQueryChangedEvent -> searchCity(event.query)
            UpdateCitySearchEvent ->
                searchCity(state.value.details.value.supportedDetails.city.searchItem.value)

            is LangSearchQueryChangedEvent -> searchLang(event.query, event.type)
            is UpdateLangSearchEvent ->
                searchLang(state.value.language.value.nativeLanguages.searchItem.value, event.type)

            is LanguagesUpdateEvent -> updateLanguages(event.lang, event.type)
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

    private fun saveUpdates() {
        reduce { it.copy(saveState = ProfileEditorState.SaveState.Loading) }
        mapAndReduceException(
            action = {
                saveProfileUseCase {
                    it.updated(state.value)
                }
                reduce { it.copy(saveState = ProfileEditorState.SaveState.Idle) }
            },
            mapper = {
                it // TODO
            },
            update = { profileState, _ ->
                profileState
            }
        )
    }

    private fun updateName(name: String) {
        reduce {
            it.updateName(name)
        }
    }

    private fun trimName() {
        val name = state.value.name.value
        val trimmedName = name.trim()
        updateName(trimmedName)
    }

    private fun updateAboutMe(aboutMe: String) {
        reduce {
            it.updateAboutMe(aboutMe)
        }
    }

    private fun updateLoockingForData(data: LookingForData) {
        reduce {
            it.updateLookingForData(data)
        }
    }

    private fun updateDetails(supportedDetails: SupportedDetails) {
        reduce {
            it.updateDetails(supportedDetails)
        }
    }

    private fun updateLifestyle(lifestyles: SupportedLifestyles) {
        reduce {
            it.updateLifestyle(lifestyles)
        }
    }

    private fun updateLanguages(languagesField: LanguageAssetsField, type: LanguagesAssetType) {
        reduce {
            it.changeLanguage(languagesField, type)
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

    private fun searchCity(query: String) {
        reduce {
            it.updateDetailCity {
                copy(
                    foundedAssets = FoundedAssets.Searching,
                    searchItem = searchItem.copy(value = query)
                )
            }
        }
        mapAndReduceException(
            action = {
                val cities = findCitiesUseCase(query)
                    .map {
                        Text(
                            id = Text.Id(it.id),
                            type = Text.Type.City,
                            default = it.name
                        )
                    } // TODO add mapper
                    .toPersistentList()
                reduce {
                    it.updateDetailCity {
                        copy(foundedAssets = FoundedAssets.Success(cities))
                    }
                }
            },
            mapper = { TODO(it.toString()) },
            update = { state, _ -> state }
        )
    }

    private fun searchLang(query: String, type: LanguagesAssetType) {
        searchLang(query) { updated ->
            updateLanguage(updated, type)
        }
    }

    private fun searchLang(
        query: String,
        updateLang: ProfileEditorState.(LanguageAssetsField.() -> LanguageAssetsField) -> ProfileEditorState
    ) {
        reduce {
            it.updateLang {
                copy(
                    foundedAssets = FoundedAssets.Searching,
                    searchItem = searchItem.copy(value = query)
                )
            }
        }
        mapAndReduceException(
            action = {
                val texts = findLanguageAssetsUseCase(query)
                    .toPersistentList()
                reduce {
                    it.updateLang {
                        copy(foundedAssets = FoundedAssets.Success(texts))
                    }
                }
            },
            mapper = { TODO(it.toString()) },
            update = { state, _ -> state }
        )
    }

    private fun deleteImage(image: Image) = reduce {
        it.deleteImage(image)
    }

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
