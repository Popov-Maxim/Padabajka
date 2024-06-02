package com.fp.padabajka.feature.messenger.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.messenger.presentation.element.Chat
import com.fp.padabajka.feature.messenger.presentation.element.MessageField

@Composable
fun MessengerScreen(component: MessengerComponent) {
    val state = component.state.subscribeAsState()
    val followNewItems = remember {
        mutableStateOf(true)
    }

    Column(verticalArrangement = Arrangement.Bottom) {
        Chat(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            items = { state.value.messengerItems },
            loadingState = { state.value.chatLoadingState },
            followNewItems = followNewItems,
            onEvent = component::onEvent
        )
        MessageField(
            modifier = Modifier.fillMaxWidth(),
            text = { state.value.nextMessageText },
            followNewItems = followNewItems,
            onEvent = component::onEvent
        )
    }
}
