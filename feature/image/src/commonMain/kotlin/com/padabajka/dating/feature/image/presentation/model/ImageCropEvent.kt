package com.padabajka.dating.feature.image.presentation.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

sealed interface ImageCropEvent {
    data object NavigateBack : ImageCropEvent
    data class ContainerSizeChanged(val size: IntSize) : ImageCropEvent
    data class Transform(val centroid: Offset, val pan: Offset, val zoom: Float) : ImageCropEvent
    data object Apply : ImageCropEvent
}
