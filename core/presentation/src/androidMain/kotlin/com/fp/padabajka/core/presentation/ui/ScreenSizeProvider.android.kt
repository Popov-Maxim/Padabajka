package com.fp.padabajka.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

actual object ScreenSizeProvider {
    @Composable
    actual fun getDpScreenSize(): DpSize {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        val screenWidth = LocalConfiguration.current.screenWidthDp

        return DpSize(screenWidth.dp, screenHeight.dp)
    }

    @Composable
    actual fun getPxScreenSize(): IntSize {
        val density = LocalDensity.current

        with(density) {
            return getDpScreenSize().toSize().toIntSize()
        }
    }
}
