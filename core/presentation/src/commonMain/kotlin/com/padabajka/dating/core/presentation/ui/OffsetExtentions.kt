package com.padabajka.dating.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

fun Offset.toIntOffset(): IntOffset {
    return IntOffset(x.roundToInt(), y.roundToInt())
}

fun Size.toIntSize(): IntSize {
    return IntSize(width.roundToInt(), height.roundToInt())
}

@Composable
fun IntSize.toDpSize(): DpSize {
    val density = LocalDensity.current.density
    return this.toDpSize(density)
}

fun IntSize.toDpSize(density: Float): DpSize {
    return DpSize((width / density).toInt().dp, (height / density).toInt().dp)
}
