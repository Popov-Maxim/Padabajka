package com.padabajka.dating.feature.messenger.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.CustomScaffold
import com.padabajka.dating.core.presentation.ui.ProfileAvatar
import com.padabajka.dating.core.presentation.ui.drawable.icon.CoreIcons
import com.padabajka.dating.core.presentation.ui.modifier.innerShadow
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NavigateBackEvent
import com.padabajka.dating.feature.messenger.presentation.chat.ui.Chat
import com.padabajka.dating.feature.messenger.presentation.chat.ui.MessageField
import com.padabajka.dating.feature.messenger.presentation.model.PersonItem

@Composable
fun ChatScreen(component: ChatComponent) {
    val state by component.state.subscribeAsState()
    val followNewItems = remember {
        mutableStateOf(true)
    }

    CustomScaffold(
        topBar = {
            TopBar(state.person, component::onEvent)
        },
        bottomBar = {
            MessageField(
                modifier = Modifier.fillMaxWidth(),
                text = state.nextMessageText,
                followNewItems = followNewItems,
                onEvent = component::onEvent
            )
        }
    ) {
        Chat(
            modifier = Modifier
                .fillMaxWidth().innerShadow(
                    color = Color(color = 0xFFA1A1A1),
                ),
            items = state.messengerItems,
            loadingState = state.chatLoadingState,
            followNewItems = followNewItems,
            onEvent = component::onEvent
        )
    }
}

@Composable
private fun TopBar(person: PersonItem, onEvent: (MessengerEvent) -> Unit) {
    Row(
        modifier = Modifier.padding(9.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(onClick = { onEvent(NavigateBackEvent) }) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = CoreIcons.BackArrow,
                contentDescription = "Back",
            )
        }

        ProfileAvatar(
            model = person.image.value,
            modifier = Modifier.size(48.dp)
        )

        Column {
            Text(
                text = person.name,
                fontSize = 16.sp,
            )
            Text(
                text = "online",
                fontSize = 10.sp
            )
        }
    }
}
