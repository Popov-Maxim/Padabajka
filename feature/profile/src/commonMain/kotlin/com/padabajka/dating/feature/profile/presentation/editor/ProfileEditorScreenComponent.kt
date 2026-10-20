package com.padabajka.dating.feature.profile.presentation.editor

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.domain.Factory
import com.padabajka.dating.core.domain.delegate
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.error.toTextError
import com.padabajka.dating.core.presentation.event.AlertService
import com.padabajka.dating.core.presentation.event.consumed
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.repository.api.ProfileRepository
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Profile
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.image.domain.GetLocalImageUseCase
import com.padabajka.dating.feature.image.presentation.mapImageError
import com.padabajka.dating.feature.profile.domain.SaveUpdatedProfileUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindCitiesUseCase
import com.padabajka.dating.feature.profile.domain.asset.FindInterestAssetsUseCase
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
import com.padabajka.dating.feature.profile.presentation.editor.model.ImageSelectedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.InterestSearchQueryChangedEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.InterestUpdateEvent
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
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateInterestSearchEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.UpdateLangSearchEvent
import com.padabajka.dating.feature.profile.presentation.editor.model.toEditorState
import com.padabajka.dating.feature.profile.presentation.editor.model.updated
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.first

class ProfileEditorScreenComponent(
    context: ComponentContext,
    private val navigateBack: () -> Unit,
    private val cropImage: (image: Image.Local, index: Int) -> Unit,
    private val profileRepository: ProfileRepository,
    saveUpdatedProfileUseCaseFactory: Factory<SaveUpdatedProfileUseCase>,
    getLocalImageUseCaseFactory: Factory<GetLocalImageUseCase>,
    private val findCitiesUseCase: FindCitiesUseCase,
    private val findLanguageAssetsUseCase: FindLanguageAssetsUseCase,
    private val findInterestAssetsUseCase: FindInterestAssetsUseCase,
    private val alertService: AlertService
) : BaseComponent<ProfileEditorState>(
    context,
    "profile_editor",
    initProfileState(profileRepository)
) {

    private val profile: Profile?
        get() = profileRepository.profileValue

    private val saveProfileUseCase by saveUpdatedProfileUseCaseFactory.delegate()
    private val getLocalImageUseCase by getLocalImageUseCaseFactory.delegate()

    init {
        if (state.value == ProfileEditorState.Loading) {
            launchStep(
                action = {
                    val loadedProfile = profileRepository.profile.first()
                    reduce { current ->
                        if (current == ProfileEditorState.Loading) loadedProfile.toEditorState() else current
                    }
                }
            )
        }
    }

    private fun reduceEditing(
        update: (ProfileEditorState.Editing) -> ProfileEditorState.Editing
    ) = reduce { current ->
        if (current is ProfileEditorState.Editing) update(current) else current
    }

    @Suppress("CyclomaticComplexMethod")
    fun onEvent(event: ProfileEditorEvent) {
        if (event == NavigateBackEvent) {
            navigateBack()
            return
        }
        val editorState = state.value as? ProfileEditorState.Editing ?: return
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
            is ImageAddEvent -> addImage(event.image, event.index)
            is ImageSelectedEvent -> cropImage(event.image, event.index)
            ConsumeInternalErrorEvent -> consumeInternalError()
            NavigateBackEvent -> Unit
            is DeleteImageEvent -> deleteImage(event.image)
            is LookingForUpdateEvent -> updateLoockingForData(event.data)
            is DetailUpdateEvent -> updateDetails(event.supportedDetails)
            is LifeStyleUpdateEvent -> updateLifestyle(event.lifestyle)
            is CitySearchQueryChangedEvent -> searchCity(event.query)
            UpdateCitySearchEvent ->
                searchCity(editorState.fields.details.value.supportedDetails.city.searchItem.value)

            is LangSearchQueryChangedEvent -> searchLang(event.query.value, event.type)
            is UpdateLangSearchEvent ->
                searchLang(editorState.fields.language.value.nativeLanguages.searchItem.value, event.type)
            is LanguagesUpdateEvent -> updateLanguages(event.lang, event.type)

            is InterestSearchQueryChangedEvent -> searchInterests(event.query.value)
            is InterestUpdateEvent -> updateInterests(event.assets)
            UpdateInterestSearchEvent ->
                searchInterests(editorState.fields.language.value.nativeLanguages.searchItem.value)
        }
    }

    private fun makeAchievementMain(achievement: Achievement?) {
        reduceEditing {
            it.updateFields(profile) {
                it.changeAchievementMain(achievement)
            }
        }
    }

    private fun makeAchievementVisible(achievement: Achievement) {
        reduceEditing {
            it.updateFields(profile) {
                it.makeAchievementVisible(achievement)
            }
        }
    }

    private fun hideAchievement(achievement: Achievement) {
        reduceEditing {
            it.updateFields(profile) {
                it.hideAchievement(achievement)
            }
        }
    }

    private fun discardUpdates() =
        launchStep(
            action = {
                profileRepository.profileValue?.let { profile ->
                    reduce {
                        profile.toEditorState()
                    }
                }
            },
        )

    private fun saveUpdates() {
        val editorState = state.value as? ProfileEditorState.Editing ?: return
        if (editorState.canSave.not()) return

        reduceEditing { it.copy(saveState = ProfileEditorState.SaveState.Loading) }
        launchStep(
            action = {
                val savedProfile = saveProfileUseCase {
                    it.updated(editorState)
                }
                reduce { savedProfile.toEditorState() }
            },
            onError = {
                val error = it.toTextError(::mapImageError)

                alertService.showAlert { error.text.translate() }
                reduceEditing { it.copy(saveState = ProfileEditorState.SaveState.Idle) }
                error.needLog.not()
            }
        )
    }

    private fun updateName(name: String) {
        reduceEditing {
            it.updateFields(profile) {
                it.updateName(name)
            }
        }
    }

    private fun trimName() {
        val name = (state.value as? ProfileEditorState.Editing)?.fields?.name?.value ?: return
        val trimmedName = name.trim()
        updateName(trimmedName)
    }

    private fun updateAboutMe(aboutMe: String) {
        reduceEditing {
            it.updateFields(profile) {
                it.updateAboutMe(aboutMe)
            }
        }
    }

    private fun updateLoockingForData(data: LookingForData) {
        reduceEditing {
            it.updateFields(profile) {
                it.updateLookingForData(data)
            }
        }
    }

    private fun updateDetails(supportedDetails: SupportedDetails) {
        reduceEditing {
            it.updateFields(profile) {
                it.updateDetails(supportedDetails)
            }
        }
    }

    private fun updateLifestyle(lifestyles: SupportedLifestyles) {
        reduceEditing {
            it.updateFields(profile) {
                it.updateLifestyle(lifestyles)
            }
        }
    }

    private fun updateLanguages(languagesField: LanguageAssetsField, type: LanguagesAssetType) {
        reduceEditing {
            it.updateFields(profile) {
                it.changeLanguage(languagesField, type)
            }
        }
    }

    private fun updateInterests(assets: PersistentList<Text>) {
        reduceEditing {
            it.updateFields(profile) {
                it.changeInterests(assets)
            }
        }
    }

    private fun addImage(image: Image, index: Int) = launchStep(
        action = {
            val uiImage = if (image is Image.Local) {
                getLocalImageUseCase(image)
            } else {
                image
            }
            reduceEditing {
                it.updateFields(profile) {
                    it.addImage(uiImage, index)
                }
            }
        },
        onError = {
            val error = it.toTextError(::mapImageError)

            alertService.showAlert { error.text.translate() }
            error.needLog.not()
        }
    )

    private fun searchCity(query: String) {
        reduceEditing {
            it.updateFields(profile) {
                it.updateDetailCity {
                    copy(
                        foundedAssets = FoundedAssets.Searching,
                        searchItem = searchItem.copy(value = query)
                    )
                }
            }
        }
        launchStep(
            action = {
                val cities = findCitiesUseCase(query)
                    .map {
                        Text(
                            id = Text.Id(it.id),
                            type = Text.Type.City,
                            default = it.name
                        )
                    }
                    .toPersistentList()
                reduceEditing {
                    it.updateFields(profile) {
                        it.updateDetailCity {
                            copy(foundedAssets = FoundedAssets.Success(cities))
                        }
                    }
                }
            },
        )
    }

    private fun searchLang(query: String, type: LanguagesAssetType) {
        searchLang(query) { updated ->
            updateFields(profile) {
                it.updateLanguage(updated, type)
            }
        }
    }

    private fun searchLang(
        query: String,
        updateLang: ProfileEditorState.Editing.(
            LanguageAssetsField.() -> LanguageAssetsField
        ) -> ProfileEditorState.Editing
    ) {
        reduceEditing {
            it.updateLang {
                copy(
                    foundedAssets = FoundedAssets.Searching,
                    searchItem = searchItem.copy(value = query)
                )
            }
        }
        launchStep(
            action = {
                val texts = findLanguageAssetsUseCase(query)
                    .toPersistentList()
                reduceEditing {
                    it.updateLang {
                        copy(foundedAssets = FoundedAssets.Success(texts))
                    }
                }
            },
        )
    }

    private fun searchInterests(query: String) {
        reduceEditing {
            it.updateFields(profile) {
                it.updateInterests {
                    copy(
                        foundedAssets = FoundedAssets.Searching,
                        searchItem = searchItem.copy(value = query)
                    )
                }
            }
        }
        launchStep(
            action = {
                val assets = findInterestAssetsUseCase(query)
                    .toPersistentList()
                reduceEditing {
                    it.updateFields(profile) {
                        it.updateInterests {
                            copy(foundedAssets = FoundedAssets.Success(assets))
                        }
                    }
                }
            },
        )
    }

    private fun deleteImage(image: Image) = reduceEditing {
        it.updateFields(profile) {
            it.deleteImage(image)
        }
    }

    private fun trimAboutMe() {
        val aboutMe = (state.value as? ProfileEditorState.Editing)?.fields?.aboutMe?.value ?: return
        val trimmedAboutMe = aboutMe.trim()
        updateAboutMe(trimmedAboutMe)
    }

    private fun consumeInternalError() = reduceEditing {
        it.copy(internalErrorStateEvent = consumed)
    }

    companion object {
        private fun initProfileState(profileRepository: ProfileRepository): ProfileEditorState {
            return profileRepository.profileValue?.toEditorState() ?: ProfileEditorState.Loading
        }
    }
}
