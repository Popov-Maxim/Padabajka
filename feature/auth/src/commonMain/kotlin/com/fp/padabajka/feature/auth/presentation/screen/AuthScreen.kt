package com.fp.padabajka.feature.auth.presentation.screen

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.auth.presentation.AuthComponent
import com.fp.padabajka.feature.auth.presentation.model.LoggingInState
import com.fp.padabajka.feature.auth.presentation.model.RegisteringState

@Composable
fun AuthScreen(component: AuthComponent) {
    val state = component.state.subscribeAsState()

    when (state.value) {
        is LoggingInState -> LoginScreen(state.value as LoggingInState, component::onEvent)
        is RegisteringState -> RegisterScreen(state.value as RegisteringState, component::onEvent)
    }
}
