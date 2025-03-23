package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.fp.padabajka.core.presentation.isDebugBuild
import com.fp.padabajka.core.presentation.ui.ScreenSizeProvider
import com.fp.padabajka.core.presentation.ui.toIntOffset
import com.fp.padabajka.feature.swiper.presentation.screen.card.CardController
import com.fp.padabajka.feature.swiper.presentation.screen.card.rememberCardController
import kotlin.math.sqrt

@Composable
fun AnimationCard(
    modifier: Modifier = Modifier,
    swipeHorizontalThreshold: Float = 300f,
    swipeVerticalThreshold: Float = 400f,
    onSwipe: (Swipe) -> Unit,
    onEndSwipeAnimation: () -> Unit,
    content: @Composable (CardController) -> Unit
) {
    val screenSize = ScreenSizeProvider.getPxScreenSize()
    var offset by remember { mutableStateOf(AnimationOffset.Zero) }
    val animationOffset by animateOffsetAsState(
        targetValue = offset.offset,
    ) {
        offset.finishedListener?.invoke(it)
        offset = offset.copy(finishedListener = null)
    }
    var rectForMinOffset: Rect by remember { mutableStateOf(Rect.Zero) }
    val controller = rememberCardController { swipe ->
        val offsetForSwipe =
            offset.getOffsetForSwipe(swipe, rectForMinOffset)
        offset = AnimationOffset(offsetForSwipe, true) {
            onEndSwipeAnimation()
        }
        onSwipe(swipe)
    }

    if (isDebugBuild()) {
        BorderForTest(rectForMinOffset)
    }
    if (rectForMinOffset == Rect.Zero || rectForMinOffset.containsIn(animationOffset)) {
        Box(
            modifier = modifier.offset {
                animationOffset.toIntOffset()
            }.graphicsLayer(
                rotationZ = rotationZ(animationOffset)
            ).pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        offset.getSwipe(swipeHorizontalThreshold, swipeVerticalThreshold)
                            .let { swipe ->
                                val offsetForSwipe =
                                    offset.getOffsetForSwipe(swipe, rectForMinOffset)
                                offset = AnimationOffset(offsetForSwipe, true) {
                                    if (swipe != null) {
                                        onEndSwipeAnimation()
                                    }
                                }
                                swipe?.let(onSwipe)
                            }
                    }
                ) { change, dragAmount ->
                    offset += dragAmount

                    if (change.positionChange() != Offset.Zero) change.consume()
                }
            }.onGloballyPositioned {
                if (rectForMinOffset == Rect.Zero) {
                    val startPosition = it.positionInRoot()
                    val cardSize = it.size

                    rectForMinOffset = getRectForMinOffset(screenSize, startPosition, cardSize)
                }
            }
        ) {
            content(controller)
        }
    }
}

private fun getRectForMinOffset(
    screenSize: IntSize,
    startPosition: Offset,
    cardSize: IntSize
): Rect {
    val maxSize = cardSize.run { sqrt((width * width + height * height).toDouble()) } / 2 + SAFE_ZONE
    val absoluteStartPosition = startPosition.run { copy(x + cardSize.width / 2, y + cardSize.height / 2) }

    val left = -(absoluteStartPosition.x + maxSize)
    val top = -(absoluteStartPosition.y + maxSize)
    val right = screenSize.width - absoluteStartPosition.x + maxSize
    val bottom = screenSize.height - absoluteStartPosition.y + maxSize

    return Rect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
}

private fun rotationZ(offset: Offset): Float {
    return offset.x / ROTATION_Z_COEFFICIENT
}

@Composable
private fun BorderForTest(rectForMinOffset: Rect) {
    if (rectForMinOffset != Rect.Zero) {
        Box(
            modifier = Modifier.height(rectForMinOffset.height.dp)
                .offset {
                    Offset(rectForMinOffset.left, 0f).toIntOffset()
                }
        )
        Box(
            modifier = Modifier.height(rectForMinOffset.height.dp)
                .offset {
                    Offset(rectForMinOffset.right, 0f).toIntOffset()
                }
        )

        Box(
            modifier = Modifier.width(rectForMinOffset.width.dp)
                .offset {
                    Offset(0f, rectForMinOffset.top).toIntOffset()
                }
        )
        Box(
            modifier = Modifier.width(rectForMinOffset.width.dp)
                .offset {
                    Offset(0f, rectForMinOffset.bottom).toIntOffset()
                }
        )
    }
}

private fun Rect.containsIn(offset: Offset): Boolean {
    return offset.x > left && offset.x < right && offset.y > top && offset.y < bottom
}

private const val SAFE_ZONE = 10
private const val ROTATION_Z_COEFFICIENT = 30
