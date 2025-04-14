package com.padabajka.dating.feature.messenger.presentation.model.item

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.messenger.ParentMessage
import kotlinx.datetime.LocalDateTime

@Immutable
sealed interface MessageItem : MessengerItem {
    override val key: Any
        get() = id.raw
    val id: MessageId
    val content: String
    val sentTime: LocalDateTime
    val hasBeenRead: Boolean
    val reaction: MessageReaction?
    val parentMessage: ParentMessageItem?
}

@Immutable
data class OutgoingMessageItem(
    override val id: MessageId,
    override val content: String,
    override val sentTime: LocalDateTime,
    override val hasBeenRead: Boolean,
    override val reaction: MessageReaction?,
    override val parentMessage: ParentMessageItem?
) : MessageItem

@Immutable
data class IncomingMessageItem(
    override val id: MessageId,
    override val content: String,
    override val sentTime: LocalDateTime,
    override val hasBeenRead: Boolean,
    override val reaction: MessageReaction?,
    override val parentMessage: ParentMessageItem?
) : MessageItem

fun Message.toMessageItem(): MessageItem {
    return when (direction) {
        MessageDirection.OUTGOING -> OutgoingMessageItem(
            id = id,
            content = content,
            sentTime = creationTime,
            hasBeenRead = status.hasBeenRead(),
            reaction = reaction,
            parentMessage = parentMessage?.toItem()
        )
        MessageDirection.INCOMING -> IncomingMessageItem(
            id = id,
            content = content,
            sentTime = creationTime,
            hasBeenRead = status.hasBeenRead(),
            reaction = reaction,
            parentMessage = parentMessage?.toItem()
        )
    }
}

private fun MessageStatus.hasBeenRead(): Boolean {
    return when (this) {
        MessageStatus.Read -> true
        else -> false
    }
}

private fun ParentMessage.toItem(): ParentMessageItem {
    return ParentMessageItem(
        id = id,
        content = content,
        direction = direction
    )
}
