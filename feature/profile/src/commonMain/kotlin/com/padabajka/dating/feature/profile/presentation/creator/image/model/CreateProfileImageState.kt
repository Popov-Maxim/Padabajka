package com.padabajka.dating.feature.profile.presentation.creator.image.model

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.profile.Image

data class CreateProfileImageState(
    val image: Image?
) : State
