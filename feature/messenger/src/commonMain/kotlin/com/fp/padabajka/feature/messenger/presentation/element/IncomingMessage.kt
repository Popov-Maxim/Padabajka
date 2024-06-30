package com.fp.padabajka.feature.messenger.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fp.padabajka.core.presentation.hourMinutes
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.feature.messenger.presentation.model.MessageGotReadEvent
import com.fp.padabajka.feature.messenger.presentation.model.MessengerEvent
import com.fp.padabajka.feature.messenger.presentation.model.ReactToMessageEvent
import com.fp.padabajka.feature.messenger.presentation.model.item.IncomingMessageItem

@Composable
fun IncomingMessage(
    message: IncomingMessageItem,
    onEvent: (MessengerEvent) -> Unit
) {
    SideEffect {
        if (message.hasBeenRead.not()) {
            onEvent(MessageGotReadEvent(message.id))
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .background(if (message.reaction == MessageReaction.Like) Color.Red else Color.Blue)
                .clickable {
                    // TODO: Add final ui and implementation for reactions
                    if (message.reaction == null) {
                        onEvent(ReactToMessageEvent(message.id, reaction = MessageReaction.Like))
                    }
                }
                .padding(8.dp)
        ) {
            Column {
                Text(message.sentTime.hourMinutes)
                Text(message.content)
            }
        }
    }
}
