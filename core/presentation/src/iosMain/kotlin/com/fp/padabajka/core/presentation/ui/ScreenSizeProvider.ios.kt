package com.fp.padabajka.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

actual object ScreenSizeProvider {
    @Composable
    actual fun getDpScreenSize(): DpSize {
        val density = LocalDensity.current.density

        return getPxScreenSize().toDp(density)
    }

    private fun IntSize.toDp(density: Float): DpSize {
        return DpSize((width / density).toInt().dp, (height / density).toInt().dp)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    actual fun getPxScreenSize(): IntSize {
        return LocalWindowInfo.current.containerSize
    }
}
