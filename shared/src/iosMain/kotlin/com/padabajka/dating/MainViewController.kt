package com.padabajka.dating

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("FunctionNaming")
fun MainViewController() =
    ComposeUIViewController {
        val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
        val focusManager = LocalFocusManager.current

        val backDispatcher = remember { BackDispatcher() }
        BackHandler(true) {
            if (imeBottom > 0) {
                focusManager.clearFocus()
            } else {
                backDispatcher.back()
            }
        }
        App(
            DefaultComponentContext(
                lifecycle = LifecycleRegistry(),
                backHandler = backDispatcher
            )
        )
    }
