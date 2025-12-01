package com.padabajka.dating.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

fun Offset.toIntOffset(): IntOffset {
    return IntOffset(x.roundToInt(), y.roundToInt())
}

fun Size.toIntSize(): IntSize {
    return IntSize(width.roundToInt(), height.roundToInt())
}

@Composable
fun IntSize.toDpSize(): DpSize {
    return DpSize(width.toDp(), height.toDp())
}

@Composable
fun Int.toDp(): Dp {
    with(LocalDensity.current) {
        return this@toDp.toDp()
    }
}

@Composable
fun Dp.toPx(): Int {
    with(LocalDensity.current) {
        return this@toPx.toPx().roundToInt()
    }
}
