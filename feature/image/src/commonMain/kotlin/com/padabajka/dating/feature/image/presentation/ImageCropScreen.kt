package com.padabajka.dating.feature.image.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreCircularProgressIndicator
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.image.raw
import com.padabajka.dating.core.presentation.ui.layout.SimpleTopBar
import com.padabajka.dating.core.presentation.ui.utils.rememberImageLoader
import com.padabajka.dating.core.repository.api.model.profile.Size
import com.padabajka.dating.feature.image.presentation.model.ImageCropData
import com.padabajka.dating.feature.image.presentation.model.ImageCropEvent
import kotlin.math.roundToInt

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
    val state by component.state.subscribeAsState()
    val painter = rememberAsyncImagePainter(state.image.raw(), imageLoader = rememberImageLoader())
    val painterState by painter.state.collectAsState()
    val isReady = painterState is AsyncImagePainter.State.Success
    val geometry = ImageCropGeometry.create(state.image.data.size, state.cropData.containerSize)

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            ImageCropper(
                imageSize = state.image.data.size,
                painter = painter,
                isReady = isReady,
                hasError = painterState is AsyncImagePainter.State.Error,
                modifier = Modifier.aspectRatio(RATIO_FOR_IMAGE),
                cropData = state.cropData,
                onEvent = component::onEvent,
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
        ) {
            CoreCallToActionButton(
                text = StaticTextId.UiId.Apply.translate(),
                enabled = isReady && geometry != null,
                onClick = {
                    component.onEvent(ImageCropEvent.Apply)
                }
            )
        }
    }
}

@Composable
private fun ImageCropper(
    imageSize: Size,
    painter: Painter,
    isReady: Boolean,
    hasError: Boolean,
    cropData: ImageCropData,
    modifier: Modifier = Modifier,
    onEvent: (ImageCropEvent) -> Unit
) {
    val onEvent by rememberUpdatedState(onEvent)
    val isReady by rememberUpdatedState(isReady)

    Box(
        modifier = modifier
            .onSizeChanged { newContainerSize ->
                onEvent(ImageCropEvent.ContainerSizeChanged(newContainerSize))
            }
            .clipToBounds()
            .pointerInput(imageSize) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    if (isReady) onEvent(ImageCropEvent.Transform(centroid, pan, zoom))
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val containerSize = IntSize(size.width.roundToInt(), size.height.roundToInt())
            val geometry = ImageCropGeometry.create(imageSize, containerSize) ?: return@Canvas
            val placement = geometry.placement(cropData)
            translate(left = placement.topLeft.x, top = placement.topLeft.y) {
                with(painter) { draw(size = placement.size) }
            }
        }
        if (hasError) {
            Text(
                text = StaticTextId.UiId.ImageReadErrorDescription.translate(),
                modifier = Modifier.padding(20.dp),
            )
        } else if (!isReady) {
            CoreCircularProgressIndicator()
        }
    }
}

private const val RATIO_FOR_IMAGE = 3f / 4
