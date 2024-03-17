package com.fp.padabajka.feature.profile.presentation.model

import com.fp.padabajka.core.repository.api.model.profile.Achievement

sealed interface ProfileEvent

data object SaveProfileUpdatesClickEvent : ProfileEvent
data object DiscardProfileUpdatesClickEvent : ProfileEvent

data class FirstNameFieldUpdateEvent(val firstName: String) : ProfileEvent
data object FirstNameFieldLoosFocusEvent : ProfileEvent
data class LastNameFieldUpdateEvent(val lastName: String) : ProfileEvent
data object LastNameFieldLoosFocusEvent : ProfileEvent
data class AboutMeFieldUpdateEvent(val aboutMe: String) : ProfileEvent
data object AboutMeFieldLoosFocusEvent : ProfileEvent

data class HideAchievementClickEvent(val achievement: Achievement) : ProfileEvent
data class MakeAchievementVisibleClickEvent(val achievement: Achievement) : ProfileEvent
data class MakeAchievementMainClickEvent(val achievement: Achievement) : ProfileEvent
data object RemoveMainAchievementClickEvent : ProfileEvent

data object ConsumeInternalErrorEvent : ProfileEvent
