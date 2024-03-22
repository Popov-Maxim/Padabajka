package com.fp.padabajka.feature.swiper.presentation.model

import androidx.compose.runtime.Immutable
import com.fp.padabajka.core.presentation.State
import com.fp.padabajka.core.repository.api.model.profile.Achievement
import com.fp.padabajka.core.repository.api.model.profile.Detail
import com.fp.padabajka.core.repository.api.model.profile.Image
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.LocalDate

@Immutable
data class SwiperState(
    val foregroundPersonItem: PersonItem,
    val backgroundPersonItem: PersonItem?
) : State

@Immutable
data class PersonItem(
    val id: PersonId,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val images: PersistentList<Image>,
    val aboutMe: String,
    val details: PersistentList<Detail>,
    val mainAchievement: Achievement?,
    val achievements: PersistentList<Achievement>
)
