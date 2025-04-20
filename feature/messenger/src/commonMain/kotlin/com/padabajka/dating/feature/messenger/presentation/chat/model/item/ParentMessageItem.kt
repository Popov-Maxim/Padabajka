package com.padabajka.dating.feature.messenger.presentation.chat.model.item

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

@Immutable
data class ParentMessageItem(
    val id: MessageId,
    val content: String,
    val direction: MessageDirection
)

fun MessageItem.toParentMessageItem(): ParentMessageItem {
    val direction = when (this) {
        is OutgoingMessageItem -> MessageDirection.OUTGOING
        is IncomingMessageItem -> MessageDirection.INCOMING
    }
    return ParentMessageItem(
        id = id,
        content = content,
        direction = direction
    )
}
