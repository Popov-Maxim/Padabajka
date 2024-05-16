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
import androidx.compose.ui.geometry.Rect
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

fun AnimationOffset.getOffsetForSwipe(swipe: Swipe?, rectForMinOffset: Rect = Rect.Zero): Offset =
    offset.getOffsetForSwipe(swipe, rectForMinOffset)

// TODO(swiper): improve logic
private fun Offset.getOffsetForSwipe(swipe: Swipe?, rectForMinOffset: Rect): Offset {
    return when (swipe) {
        Swipe.Left,
        Swipe.Right,
        Swipe.Up -> rectForMinOffset.getOffsetForSwipe(Offset(x * POSITION_SCALE, y * POSITION_SCALE))
        null -> Offset.Zero
    }
}

@Suppress("NestedBlockDepth")
private fun Rect.getOffsetForSwipe(offsetForSwipe: Offset): Offset {
    val (x, y) = offsetForSwipe

    val contains = contains(offsetForSwipe)
    return if (contains.not()) {
        offsetForSwipe
    } else {
        if (x.toInt() == 0) {
            if (y > 0) {
                Offset(x, bottom)
            } else {
                Offset(x, top)
            }
        } else if (x > 0) {
            if (y.toInt() == 0) {
                Offset(right, y)
            } else if (y > 0) {
                val k = bottom / y
                val newX = x * k
                if (newX <= right) {
                    Offset(newX, bottom)
                } else {
                    Offset(right, y * right / x)
                }
            } else {
                val k = top / y
                val newX = x * k
                if (newX <= right) {
                    Offset(newX, top)
                } else {
                    Offset(right, y * right / x)
                }
            }
        } else {
            if (y.toInt() == 0) {
                Offset(left, y)
            } else if (y > 0) {
                val k = bottom / y
                val newX = x * k
                if (newX >= left) {
                    Offset(newX, bottom)
                } else {
                    Offset(left, y * left / x)
                }
            } else {
                val k = top / y
                val newX = x * k
                if (newX >= left) {
                    Offset(newX, top)
                } else {
                    Offset(left, y * left / x)
                }
            }
        }
    }
}

fun AnimationOffset.getSwipe(
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

private const val POSITION_SCALE = 2.0f
