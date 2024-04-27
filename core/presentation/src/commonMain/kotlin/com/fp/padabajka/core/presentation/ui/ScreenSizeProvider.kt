package com.fp.padabajka.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize

expect object ScreenSizeProvider {
    @Composable
    fun getScreenSize(): DpSize
}
