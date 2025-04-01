package com.padabajka.dating.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

actual object ScreenSizeProvider {
    @Composable
    actual fun getDpScreenSize(): DpSize {
        val density = LocalDensity.current.density

        return getPxScreenSize().toDpSize(density)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    actual fun getPxScreenSize(): IntSize {
        return LocalWindowInfo.current.containerSize
    }
}
