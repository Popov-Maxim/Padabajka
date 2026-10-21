package com.padabajka.dating.feature.image.presentation

import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.repository.api.model.common.CoreRect
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.feature.image.presentation.model.ImageCropEvent
import com.padabajka.dating.feature.image.presentation.model.ImageCropState

class ImageCropScreenComponent(
    componentContext: ComponentContext,
    private val image: Image.Local,
    private val navigateBack: () -> Unit,
    private val onImageCropped: (CoreRect) -> Unit
) : BaseComponent<ImageCropState>(
    componentContext,
    "image_crop",
    ImageCropState(image)
) {
    fun onEvent(event: ImageCropEvent) {
        when (event) {
            ImageCropEvent.NavigateBack -> navigateBack()
            is ImageCropEvent.ContainerSizeChanged -> resize(event.size)
            is ImageCropEvent.Transform -> transform(event)
            ImageCropEvent.Apply -> apply()
        }
    }

    private fun resize(size: IntSize) = reduce { current ->
        val data = current.cropData.copy(containerSize = size)
        val geometry = ImageCropGeometry.create(image.data.size, size)
        current.copy(cropData = geometry?.constrain(data) ?: data)
    }

    private fun transform(event: ImageCropEvent.Transform) = reduce { current ->
        val geometry = ImageCropGeometry.create(image.data.size, current.cropData.containerSize)
            ?: return@reduce current
        current.copy(cropData = geometry.transform(current.cropData, event.centroid, event.pan, event.zoom))
    }

    private fun apply() {
        val data = state.value.cropData
        val geometry = ImageCropGeometry.create(image.data.size, data.containerSize) ?: return
        onImageCropped(geometry.cropRect(data))
    }
}
