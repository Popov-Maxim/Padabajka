package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import com.fp.padabajka.core.presentation.ui.toIntOffset

@Immutable
class AnimationCard(
    private val modifier: Modifier = Modifier,
    private val cardDesign: @Composable () -> Unit,
    private val onSwipe: (Swipe) -> Unit,
    private val swipeHorizontalThreshold: Float = 400f,
    private val swipeVerticalThreshold: Float = 400f,
    private val offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
) {
    @Composable
    fun draw() {
        key(this) {
            var offset by offset
            val animationOffset by animateOffsetAsState(targetValue = offset)

            Box(
                modifier = modifier.offset {
                    animationOffset.toIntOffset()
                }.pointerInput(this) {
                    detectDragGestures(onDragEnd = {
                        offset.getSwipe(swipeHorizontalThreshold, swipeVerticalThreshold)
                            .let { swipe ->
                                val offsetForSwipe = offset.getOffsetForSwipe(swipe)
                                offset = offsetForSwipe
                                swipe?.let(onSwipe)
                            }
                    }) { change, dragAmount ->

                        offset += dragAmount

                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }.graphicsLayer(
                    rotationZ = animationOffset.x / 30,
                )
            ) {
                cardDesign()
            }
        }
    }
}
