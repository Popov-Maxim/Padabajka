package com.fp.padabajka.feature.profile.presentation.model

import com.fp.padabajka.core.repository.api.model.profile.Achievement

sealed interface ProfileEvent

data object SaveProfileUpdatesClickEvent : ProfileEvent
data object DiscardProfileUpsatesClickEvent : ProfileEvent

data class FirstNameUpdateEvent(val firstName: String) : ProfileEvent
data class LastNameUpdateEvent(val lastName: String) : ProfileEvent
data class AboutMeUpdateEvent(val aboutMe: String) : ProfileEvent

data class HideAchievementClickEvent(val achievement: Achievement) : ProfileEvent
data class MakeAchievementVisibleClickEvent(val achievement: Achievement) : ProfileEvent
data class MakeAchievementMainClickEvent(val achievement: Achievement) : ProfileEvent
data object RemoveMainAchievementClickEvent : ProfileEvent

data object ConsumeInternalErrorEvent : ProfileEvent
