package com.padabajka.dating.feature.profile.presentation.creator.birthday.model

sealed interface CreateProfileBirthdayEvent {
    data class BirthdayFieldUpdate(val dateMillis: Long) : CreateProfileBirthdayEvent
    data object PressContinue : CreateProfileBirthdayEvent
}
