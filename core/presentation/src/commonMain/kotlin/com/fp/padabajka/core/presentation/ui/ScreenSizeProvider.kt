package com.fp.padabajka.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize

expect object ScreenSizeProvider {
    @Composable
    fun getScreenSize(): IntSize
}
