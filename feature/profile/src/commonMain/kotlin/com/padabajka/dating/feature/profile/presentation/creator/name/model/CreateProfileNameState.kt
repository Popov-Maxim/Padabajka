package com.padabajka.dating.feature.profile.presentation.creator.name.model

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField

data class CreateProfileNameState(
    val nameField: ProfileField<String>
) : State
