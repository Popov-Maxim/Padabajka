package com.padabajka.dating.feature.image.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import com.padabajka.dating.core.repository.api.model.common.CoreRect
import com.padabajka.dating.feature.image.presentation.model.ImageCropData
import kotlin.math.roundToInt
import com.padabajka.dating.core.repository.api.model.profile.Size as ImageSize

internal class ImageCropGeometry private constructor(
    private val imageSize: ImageSize,
    private val containerSize: IntSize,
) {
    private val baseScale = maxOf(
        containerSize.width.toFloat() / imageSize.width,
        containerSize.height.toFloat() / imageSize.height,
    )

    // Keep at least one source pixel along each side of the crop.
    private val maxScale = (minOf(containerSize.width, containerSize.height) / baseScale).coerceAtLeast(1f)

    fun constrain(data: ImageCropData): ImageCropData {
        val scale = data.scale.takeIf { it.isFinite() }?.coerceIn(1f, maxScale) ?: 1f
        val displayedWidth = imageSize.width * baseScale * scale
        val displayedHeight = imageSize.height * baseScale * scale
        val maxOffsetX = ((displayedWidth - containerSize.width) / 2f).coerceAtLeast(0f)
        val maxOffsetY = ((displayedHeight - containerSize.height) / 2f).coerceAtLeast(0f)
        return data.copy(
            scale = scale,
            offset = Offset(
                x = (data.offset.x.takeIf { it.isFinite() } ?: 0f).coerceIn(-maxOffsetX, maxOffsetX),
                y = (data.offset.y.takeIf { it.isFinite() } ?: 0f).coerceIn(-maxOffsetY, maxOffsetY),
            ),
            containerSize = containerSize,
        )
    }

    fun transform(data: ImageCropData, centroid: Offset, pan: Offset, zoom: Float): ImageCropData {
        val current = constrain(data)
        if (!zoom.isFinite() || zoom <= 0f) return current
        val scale = (current.scale * zoom).coerceIn(1f, maxScale)
        val scaleChange = scale / current.scale
        val focus = centroid - Offset(containerSize.width / 2f, containerSize.height / 2f)
        return constrain(
            current.copy(
                scale = scale,
                offset = (current.offset - focus) * scaleChange + focus + pan,
            )
        )
    }

    fun placement(data: ImageCropData): Placement {
        val current = constrain(data)
        val scale = baseScale * current.scale
        val size = Size(imageSize.width * scale, imageSize.height * scale)
        return Placement(
            size = size,
            topLeft = Offset(
                x = (containerSize.width - size.width) / 2f + current.offset.x,
                y = (containerSize.height - size.height) / 2f + current.offset.y,
            ),
            scale = scale,
        )
    }

    fun cropRect(data: ImageCropData): CoreRect {
        val placement = placement(data)
        val width = (containerSize.width / placement.scale).roundToInt().coerceIn(1, imageSize.width)
        val height = (containerSize.height / placement.scale).roundToInt().coerceIn(1, imageSize.height)
        val left = (-placement.topLeft.x / placement.scale).roundToInt().coerceIn(0, imageSize.width - width)
        val top = (-placement.topLeft.y / placement.scale).roundToInt().coerceIn(0, imageSize.height - height)
        return CoreRect(left = left, top = top, right = left + width, bottom = top + height)
    }

    data class Placement(val size: Size, val topLeft: Offset, val scale: Float)

    companion object {
        fun create(imageSize: ImageSize, containerSize: IntSize): ImageCropGeometry? {
            val validImage = imageSize.width > 0 && imageSize.height > 0
            val validContainer = containerSize.width > 0 && containerSize.height > 0
            return if (validImage && validContainer) {
                ImageCropGeometry(imageSize, containerSize)
            } else {
                null
            }
        }
    }
}
