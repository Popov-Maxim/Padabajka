package com.padabajka.dating.feature.profile.presentation.creator.birthday.model

import androidx.compose.runtime.Stable
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.age
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField
import kotlinx.datetime.LocalDate

data class CreateProfileBirthdayState(
    val birthdayField: ProfileField<BirthdayItem?>,
) : State

data class BirthdayItem(val date: LocalDate) {
    @Stable
    val age: Age = date.age
}
