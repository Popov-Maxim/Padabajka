package com.fp.padabajka.core.presentation.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

fun Offset.toIntOffset(): IntOffset {
    return IntOffset(x.roundToInt(), y.roundToInt())
}

fun Size.toIntSize(): IntSize {
    return IntSize(width.roundToInt(), height.roundToInt())
}
