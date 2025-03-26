package com.padabajka.dating

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

@Suppress("FunctionNaming")
fun MainViewController() =
    ComposeUIViewController {
        App(DefaultComponentContext(LifecycleRegistry()))
    }
