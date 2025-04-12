package com.padabajka.dating.feature.profile.presentation.model

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.profile.Achievement
import com.padabajka.dating.core.repository.api.model.profile.Detail
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.LocalDate

data class ProfileState(
    val value: ProfileValue
) : State

sealed interface ProfileValue {
    data object Error : ProfileValue
    data object Loading : ProfileValue
    data class Loaded(
        val name: String,
        val birthday: LocalDate,
        val images: PersistentList<Image>,
        val aboutMe: String,
        val lookingFor: LookingForData,
        val details: PersistentList<Detail>,
        val mainAchievement: Achievement?,
        val achievements: PersistentList<Achievement>
    ) : ProfileValue
}

fun Profile.toUIProfileValue(): ProfileValue.Loaded {
    return ProfileValue.Loaded(
        name,
        birthday,
        images,
        aboutMe,
        lookingFor,
        details,
        mainAchievement,
        achievements
    )
}
