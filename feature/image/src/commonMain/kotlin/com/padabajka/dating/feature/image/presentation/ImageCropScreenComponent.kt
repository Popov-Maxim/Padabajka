package com.padabajka.dating.feature.image.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.model.common.CoreRect
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.core.repository.api.model.profile.Size
import com.padabajka.dating.feature.image.presentation.model.ImageCropData
import com.padabajka.dating.feature.image.presentation.model.ImageCropEvent
import com.padabajka.dating.feature.image.presentation.model.ImageCropState
import kotlin.math.roundToInt

class ImageCropScreenComponent(
    componentContext: ComponentContext,
    private val image: Image.Local,
    private val navigateBack: () -> Unit,
    private val onImageCropped: (CoreRect) -> Unit
) : BaseComponent<ImageCropState>(
    componentContext,
    ImageCropState(image)
) {
    fun onEvent(event: ImageCropEvent) {
        when (event) {
            ImageCropEvent.NavigateBack -> navigateBack()
            is ImageCropEvent.ChangeCropData -> changeCropData(event.data)
            ImageCropEvent.Apply -> apply()
        }
    }

    private fun changeCropData(data: ImageCropData) {
        reduce { it.copy(cropData = data) }
    }

    private fun apply() {
        val rest = calculateCropRect(
            image.data.size,
            state.value.cropData.containerSize,
            state.value.cropData.scale,
            state.value.cropData.offset
        )
        onImageCropped(rest)
    }

    fun calculateCropRect(
        imageSize: Size,
        containerSize: IntSize,
        scale: Float,
        offset: Offset,
    ): CoreRect {
        val imageWidthPx: Int = imageSize.width
        val imageHeightPx: Int = imageSize.height
        val containerWidth: Int = containerSize.width
        val containerHeight: Int = containerSize.height
        val offsetX: Float = offset.x
        val offsetY: Float = offset.y

        val baseScale = maxOf(
            containerWidth.toFloat() / imageWidthPx,
            containerHeight.toFloat() / imageHeightPx
        )

        val totalScale = baseScale * scale

        val displayedWidth = imageWidthPx * totalScale
        val displayedHeight = imageHeightPx * totalScale

        val imageLeft = (containerWidth - displayedWidth) / 2f + offsetX
        val imageTop = (containerHeight - displayedHeight) / 2f + offsetY

        val x = (0f - imageLeft) / totalScale
        val y = (0f - imageTop) / totalScale

        val width = containerWidth / totalScale
        val height = containerHeight / totalScale

        val left = x.coerceIn(0f, imageWidthPx.toFloat())
        val top = y.coerceIn(0f, imageHeightPx.toFloat())
        val right = (x + width).coerceIn(0f, imageWidthPx.toFloat())
        val bottom = (y + height).coerceIn(0f, imageHeightPx.toFloat())

        return CoreRect(
            left = left.roundToInt(),
            top = top.roundToInt(),
            right = right.roundToInt(),
            bottom = bottom.roundToInt()
        )
    }
}
