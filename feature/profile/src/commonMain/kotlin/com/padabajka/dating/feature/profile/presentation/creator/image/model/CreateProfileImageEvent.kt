package com.padabajka.dating.feature.profile.presentation.creator.image.model

import com.padabajka.dating.core.repository.api.model.profile.Image

sealed interface CreateProfileImageEvent {
    data class ImageSelected(val image: Image) : CreateProfileImageEvent
    data object DeleteImage : CreateProfileImageEvent
    data object Continue : CreateProfileImageEvent
}
