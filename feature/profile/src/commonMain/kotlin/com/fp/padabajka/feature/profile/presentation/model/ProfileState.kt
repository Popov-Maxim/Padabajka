package com.fp.padabajka.feature.profile.presentation.model

import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.core.repository.api.model.profile.Detail
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.LocalDate

data class ProfileState(
    val value: ProfileValue
) : State

sealed interface ProfileValue {
    data object Error : ProfileValue
    data object Loading : ProfileValue
    data class Loaded(
        val firstName: String,
        val lastName: String,
        val birthday: LocalDate,
        val images: PersistentList<Image>,
        val aboutMe: String,
        val details: PersistentList<Detail>,
        val mainAchievement: Achievement?,
        val achievements: PersistentList<Achievement>
    ) : ProfileValue
}

fun Profile.toUIProfileValue(): ProfileValue.Loaded {
    return ProfileValue.Loaded(
        firstName,
        lastName,
        birthday,
        images,
        aboutMe,
        details,
        mainAchievement,
        achievements
    )
}
