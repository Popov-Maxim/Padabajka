package com.padabajka.dating.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration

@Composable
fun CustomViewConfiguration(
    doubleTapTimeoutMillis: Long,
    content: @Composable () -> Unit
) {
    val originalConfig = LocalViewConfiguration.current

    val customConfig = remember(originalConfig) {
        object : ViewConfiguration by originalConfig {
            override val doubleTapTimeoutMillis: Long
                get() = doubleTapTimeoutMillis
        }
    }

    CompositionLocalProvider(LocalViewConfiguration provides customConfig) {
        content()
    }
}
