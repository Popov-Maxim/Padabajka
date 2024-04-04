package com.fp.padabajka

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.messenger.presentation.TestMessengerComponent
import com.fp.padabajka.feature.messenger.presentation.element.Chat

@Suppress("UnusedParameter")
@Composable
fun App(rootContext: ComponentContext) {
    val component = remember {
        TestMessengerComponent(
            context = rootContext
        )
    }
    val state = component.state.subscribeAsState()
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Box {
                val recompositions = remember {
                    mutableStateOf(0)
                }
                recompositions.value++
                Text("${recompositions.value}")
            }
            Box {
                Chat(
                    modifier = Modifier.fillMaxSize(),
                    items = { state.value.items },
                    loadingState = { state.value.chatLoadingState },
                    onEvent = component::onEvent
                )
            }
        }
    }
}
