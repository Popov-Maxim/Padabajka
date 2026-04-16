package com.padabajka.dating.feature.image.presentation

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.image.CoreAsyncImage
import com.padabajka.dating.core.presentation.ui.image.raw
import com.padabajka.dating.core.presentation.ui.layout.SimpleTopBar
import com.padabajka.dating.core.repository.api.model.profile.Image
import com.padabajka.dating.feature.image.presentation.model.ImageCropData
import com.padabajka.dating.feature.image.presentation.model.ImageCropEvent

@Composable
fun ImageCropScreen(component: ImageCropScreenComponent) {
    CustomScaffold(
        topBar = {
            SimpleTopBar(
                title = StaticTextId.UiId.CropPhoto.translate(),
                navigateBack = {
                    component.onEvent(ImageCropEvent.NavigateBack)
                }
            )
        }
    ) {
        Content(component)
    }
}

@Composable
private fun Content(component: ImageCropScreenComponent) {
    Column {
        val state by component.state.subscribeAsState()

        ImageCropper(
            image = state.image,
            modifier = Modifier.fillMaxWidth().weight(1f),
            cropData = state.cropData,
            onChangeCropData = {
                component.onEvent(ImageCropEvent.ChangeCropData(it))
            }
        )

        Column(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
        ) {
            CoreCallToActionButton(
                text = StaticTextId.UiId.Apply.translate(),
                onClick = {
                    component.onEvent(ImageCropEvent.Apply)
                }
            )
        }
    }
}

@Composable
private fun ImageCropper(
    image: Image.Local,
    cropData: ImageCropData,
    modifier: Modifier = Modifier,
    onChangeCropData: (ImageCropData) -> Unit
) {
    val cropData by rememberUpdatedState(cropData)

    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .onSizeChanged { containerSize = it }
            .aspectRatio(RATIO_FOR_IMAGE)
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = (cropData.scale * zoom).coerceAtLeast(1f)

                    val scaledWidth = containerSize.width * newScale
                    val scaledHeight = containerSize.height * newScale

                    val maxOffsetX = (scaledWidth - containerSize.width) / 2f
                    val maxOffsetY = (scaledHeight - containerSize.height) / 2f

                    val newOffset = Offset(
                        x = (cropData.offset.x + pan.x).coerceIn(-maxOffsetX, maxOffsetX),
                        y = (cropData.offset.y + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                    )

                    onChangeCropData(ImageCropData(newScale, newOffset, containerSize))
                }
            }
    ) {
        CoreAsyncImage(
            model = image.raw(),
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = cropData.scale
                    scaleY = cropData.scale

                    translationX = cropData.offset.x
                    translationY = cropData.offset.y
                }
        )
    }
}

private const val RATIO_FOR_IMAGE = 3f / 4
