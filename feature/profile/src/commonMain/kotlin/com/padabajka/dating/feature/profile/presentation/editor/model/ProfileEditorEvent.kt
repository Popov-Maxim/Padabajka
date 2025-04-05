package com.padabajka.dating.feature.profile.presentation.editor.model

import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Image

sealed interface ProfileEditorEvent

data object SaveProfileUpdatesClickEvent : ProfileEditorEvent
data object DiscardProfileUpdatesClickEvent : ProfileEditorEvent

data class FirstNameFieldUpdateEvent(val firstName: String) : ProfileEditorEvent
data object FirstNameFieldLoosFocusEvent : ProfileEditorEvent
data class LastNameFieldUpdateEvent(val lastName: String) : ProfileEditorEvent
data object LastNameFieldLoosFocusEvent : ProfileEditorEvent
data class AboutMeFieldUpdateEvent(val aboutMe: String) : ProfileEditorEvent
data object AboutMeFieldLoosFocusEvent : ProfileEditorEvent

data class ImageAddEvent(val image: Image) : ProfileEditorEvent
data class DeleteImageEvent(val image: Image) : ProfileEditorEvent

data class HideAchievementClickEvent(val achievement: Achievement) : ProfileEditorEvent
data class MakeAchievementVisibleClickEvent(val achievement: Achievement) : ProfileEditorEvent
data class MakeAchievementMainClickEvent(val achievement: Achievement) : ProfileEditorEvent
data object RemoveMainAchievementClickEvent : ProfileEditorEvent

data object NavigateBackEvent : ProfileEditorEvent

data object ConsumeInternalErrorEvent : ProfileEditorEvent
