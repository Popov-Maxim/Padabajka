package com.padabajka.dating.feature.messenger.presentation.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.hourMinutes
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessageGotReadEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.ReactToMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.IncomingMessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.MessageItem
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.OutgoingMessageItem

@Composable
fun Message(
    message: MessageItem,
    shape: Shape,
    onEvent: (MessengerEvent) -> Unit
) {
    if (message is IncomingMessageItem) {
        SideEffect {
            if (message.hasBeenRead.not()) {
                onEvent(MessageGotReadEvent(message.id))
            }
        }
    }

    val textColor = when (message) {
        is IncomingMessageItem -> Color.Black
        is OutgoingMessageItem -> CoreColors.Chat.message.textColor
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .shadow(2.dp, shape)
                .backgroundForMessage(message, shape)
                .alignForMessage(message, this)
                .clickable {
                    // TODO: Add final ui and implementation for reactions
                    if (message.reaction == null) {
                        onEvent(ReactToMessageEvent(message.id, reaction = MessageReaction.Like))
                    } else {
                        onEvent(ReactToMessageEvent(message.id, reaction = null))
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 9.dp, vertical = 2.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = message.content,
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.widthIn(max = 300.dp).padding(vertical = 4.dp),
                )
                Text(message.sentTime.hourMinutes, color = textColor, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun Modifier.backgroundForMessage(
    message: MessageItem,
    shape: Shape
): Modifier {
    val backgroundColor = if (message.reaction == MessageReaction.Like) {
        Color.Red
    } else {
        when (message) {
            is IncomingMessageItem -> Color.White
            is OutgoingMessageItem -> CoreColors.Chat.message.mainColor
        }
    }

    return this.background(backgroundColor, shape)
}

@Composable
private fun Modifier.alignForMessage(
    message: MessageItem,
    boxScope: BoxScope
): Modifier {
    val alignment = when (message) {
        is IncomingMessageItem -> Alignment.CenterStart
        is OutgoingMessageItem -> Alignment.CenterEnd
    }
    with(boxScope) {
        return this@alignForMessage.align(alignment)
    }
}
