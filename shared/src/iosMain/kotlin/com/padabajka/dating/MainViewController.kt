package com.padabajka.dating

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@OptIn(ExperimentalComposeUiApi::class)
@Suppress("FunctionNaming")
fun MainViewController() =
    ComposeUIViewController {
        val backDispatcher = remember { BackDispatcher() }
        BackHandler(true) {
            backDispatcher.back()
        }
        App(
            DefaultComponentContext(
                lifecycle = LifecycleRegistry(),
                backHandler = backDispatcher
            )
        )
    }
