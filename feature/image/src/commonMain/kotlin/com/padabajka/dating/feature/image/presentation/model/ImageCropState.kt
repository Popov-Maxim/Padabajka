package com.padabajka.dating.feature.image.presentation.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.profile.Image

@Stable
data class ImageCropState(
    val image: Image.Local,
    val cropData: ImageCropData = ImageCropData()
) : State

@Stable
data class ImageCropData(
    val scale: Float = 1f,
    val offset: Offset = Offset.Zero,
    val containerSize: IntSize = IntSize.Zero
)
