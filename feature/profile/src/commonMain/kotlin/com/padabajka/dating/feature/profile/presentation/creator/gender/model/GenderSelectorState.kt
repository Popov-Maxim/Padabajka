package com.padabajka.dating.feature.profile.presentation.creator.gender.model

import com.padabajka.dating.core.presentation.State

data class GenderSelectorState(
    val userGender: GenderUI?,
    val preferredGender: PreferredGenderUI?,
) : State
