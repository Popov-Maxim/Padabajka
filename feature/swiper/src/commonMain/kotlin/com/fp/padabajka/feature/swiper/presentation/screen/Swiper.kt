package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import com.fp.padabajka.core.presentation.ui.toIntOffset

@Composable
fun SwipeCard(
    modifier: Modifier = Modifier,
    onSwipe: (Swipe) -> Unit = {},
    swipeHorizontalThreshold: Float = 400f,
    swipeVerticalThreshold: Float = 400f,
    foregroundContent: @Composable () -> Unit,
    betweenContent: @Composable (Swipe?) -> Unit,
    backgroundContent: @Composable () -> Unit
) {
    var offsetForAnimation by remember { mutableStateOf(AnimationOffset.Zero) }
    val animationOffset by animateOffsetAsState(targetValue = offsetForAnimation.offset) {
        offsetForAnimation.finishedListener?.invoke(it)
    }
    var swipe: Swipe? by remember { mutableStateOf(null) }

    Box(modifier = modifier) {
        backgroundContent()
    }
    Box(modifier = modifier) {
        betweenContent(swipe)
    }
    val resultOffset = if (offsetForAnimation.needAnimate) animationOffset else offsetForAnimation.offset
    Box(
        modifier = modifier.offset {
            resultOffset.toIntOffset()
        }.pointerInput(Unit) {
            detectDragGestures(onDragEnd = {
                offsetForAnimation.getSwipe(swipeHorizontalThreshold, swipeVerticalThreshold).let { swipe ->
                    val offsetForSwipe = offsetForAnimation.getOffsetForSwipe(swipe)
                    offsetForAnimation = offsetForAnimation.copy(offset = offsetForSwipe, needAnimate = true) {
                        offsetForAnimation = AnimationOffset.Zero
                        swipe?.let(onSwipe)
                    }
                }
            }) { change, dragAmount ->

                offsetForAnimation += dragAmount
                swipe = offsetForAnimation.getSwipe(swipeHorizontalThreshold, swipeVerticalThreshold)

                if (change.positionChange() != Offset.Zero) change.consume()
            }
        }.graphicsLayer(
            rotationZ = resultOffset.x / 30,
        )
    ) {
        foregroundContent()
    }
}

private fun AnimationOffset.getOffsetForSwipe(swipe: Swipe?): Offset =
    offset.getOffsetForSwipe(swipe)

// TODO(swiper): improve logic
fun Offset.getOffsetForSwipe(swipe: Swipe?): Offset {
    return when (swipe) {
        Swipe.Left,
        Swipe.Right,
        Swipe.Up -> copy(x * POSITION_SCALE, y * POSITION_SCALE)
        null -> Offset.Zero
    }
}

private fun AnimationOffset.getSwipe(
    swipeHorizontalThreshold: Float,
    swipeVerticalThreshold: Float
): Swipe? =
    offset.getSwipe(swipeHorizontalThreshold, swipeVerticalThreshold)

fun Offset.getSwipe(swipeHorizontalThreshold: Float, swipeVerticalThreshold: Float): Swipe? {
    return when {
        -y > swipeVerticalThreshold -> Swipe.Up
        x > swipeHorizontalThreshold -> Swipe.Right
        x < -swipeHorizontalThreshold -> Swipe.Left
        else -> null
    }
}

private const val POSITION_SCALE = 3.5f
