package com.padabajka.dating.feature.profile.presentation.editor.model

import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LookingForData

sealed interface ProfileEditorEvent

data object SaveProfileUpdatesClickEvent : ProfileEditorEvent
data object DiscardProfileUpdatesClickEvent : ProfileEditorEvent

data class NameFieldUpdateEvent(val name: String) : ProfileEditorEvent
data object NameFieldLoosFocusEvent : ProfileEditorEvent
data class AboutMeFieldUpdateEvent(val aboutMe: String) : ProfileEditorEvent
data object AboutMeFieldLoosFocusEvent : ProfileEditorEvent
data class LookingForUpdateEvent(val data: LookingForData) : ProfileEditorEvent

data class ImageAddEvent(val image: Image) : ProfileEditorEvent
data class DeleteImageEvent(val image: Image) : ProfileEditorEvent

data class HideAchievementClickEvent(val achievement: Achievement) : ProfileEditorEvent
data class MakeAchievementVisibleClickEvent(val achievement: Achievement) : ProfileEditorEvent
data class MakeAchievementMainClickEvent(val achievement: Achievement) : ProfileEditorEvent
data object RemoveMainAchievementClickEvent : ProfileEditorEvent

data class DetailUpdateEvent(val supportedDetails: SupportedDetails) : ProfileEditorEvent

data object NavigateBackEvent : ProfileEditorEvent

data class CitySearchQueryChangedEvent(val query: String) : ProfileEditorEvent
data object UpdateCitySearchEvent : ProfileEditorEvent

data object ConsumeInternalErrorEvent : ProfileEditorEvent
