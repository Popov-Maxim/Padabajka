package com.padabajka.dating.feature.image.presentation.model

sealed interface ImageCropEvent {
    data object NavigateBack : ImageCropEvent
    data class ChangeCropData(val data: ImageCropData) : ImageCropEvent
    data object Apply : ImageCropEvent
}
