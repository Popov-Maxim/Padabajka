package com.fp.padabajka.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize

actual object ScreenSizeProvider {
    @Composable
    actual fun getScreenSize(): IntSize {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val screenWidth = LocalConfiguration.current.screenWidthDp

        return IntSize(screenWidth, screenHeight)
    }
}
