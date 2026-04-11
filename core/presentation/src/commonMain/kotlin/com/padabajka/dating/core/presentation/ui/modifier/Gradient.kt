package com.padabajka.dating.core.presentation.ui.modifier

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Suppress("MagicNumber")
object Gradient {
    val rightTopOffset = Offset(0x7f800000_0f000000L)
    val leftBottomOffset = Offset(0x0f000000_7f800000L)

    val colorsForLoginScreen = listOf(
        Color(0xFFF1F1F1),
        Color(0xFFBEBAC9),
    )

    val colorsForSubScreen = listOf(
        Color(0xFFF1F1F1),
        Color(0xFFE8E5F0),
    )
}
