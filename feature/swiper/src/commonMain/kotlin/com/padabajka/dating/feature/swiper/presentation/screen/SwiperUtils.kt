package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

fun AnimationOffset.getOffsetForSwipe(swipe: Swipe?, rectForMinOffset: Rect = Rect.Zero): Offset =
    offset.getOffsetForSwipe(swipe, rectForMinOffset)

// TODO(swiper): improve logic
private fun Offset.getOffsetForSwipe(swipe: Swipe?, rectForMinOffset: Rect): Offset {
    return if (swipe == null) {
        Offset.Zero
    } else {
        if (this != Offset.Zero) {
            rectForMinOffset.getOffsetForSwipe(Offset(x * POSITION_SCALE, y * POSITION_SCALE))
        } else {
            when (swipe) {
                Swipe.Left -> Offset(rectForMinOffset.left, 0f)
                Swipe.Right -> Offset(rectForMinOffset.right, 0f)
                Swipe.Up -> Offset(0f, rectForMinOffset.top)
            }
        }
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
