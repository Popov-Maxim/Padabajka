package com.fp.padabajka.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

actual object ScreenSizeProvider {
    @Composable
    actual fun getScreenSize(): DpSize {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val screenWidth = LocalConfiguration.current.screenWidthDp

        return DpSize(screenWidth.dp, screenHeight.dp)
    }
}
