package com.fp.padabajka.feature.profile.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.core.repository.api.model.profile.Detail
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.profile.Profile
import kotlinx.datetime.LocalDate

@Immutable
data class ProfileState(
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val images: List<Image>,
    val aboutMe: String,
    val details: List<Detail>,
    val mainAchievement: Achievement?,
    val achievements: List<Achievement>
) : State

fun Profile.toUIState(): ProfileState {
    return ProfileState(
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
