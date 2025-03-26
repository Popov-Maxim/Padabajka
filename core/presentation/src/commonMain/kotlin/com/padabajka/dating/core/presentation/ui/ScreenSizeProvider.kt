package com.padabajka.dating.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

expect object ScreenSizeProvider {
    @Composable
    fun getDpScreenSize(): DpSize

    @Composable
    fun getPxScreenSize(): IntSize
}
