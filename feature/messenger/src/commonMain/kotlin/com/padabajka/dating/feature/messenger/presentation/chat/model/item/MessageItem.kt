package com.padabajka.dating.feature.messenger.presentation.chat.model.item

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageDirection
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.messenger.ParentMessage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDateTime

@Immutable
sealed interface MessageItem : MessengerItem {
    override val key: Any
        get() = id.raw
    val id: MessageId
    val content: String
    val sentTime: LocalDateTime
    val hasBeenRead: Boolean
    val readAt: LocalDateTime?
    val reactions: PersistentList<MessageReaction>
    val parentMessage: ParentMessageItem?
}

@Immutable
data class OutgoingMessageItem(
    override val id: MessageId,
    override val content: String,
    override val sentTime: LocalDateTime,
    override val hasBeenRead: Boolean,
    override val readAt: LocalDateTime?,
    override val reactions: PersistentList<MessageReaction>,
    override val parentMessage: ParentMessageItem?,
    val status: MessageStatus
) : MessageItem

@Immutable
data class IncomingMessageItem(
    override val id: MessageId,
    override val content: String,
    override val sentTime: LocalDateTime,
    override val hasBeenRead: Boolean,
    override val readAt: LocalDateTime?,
    override val reactions: PersistentList<MessageReaction>,
    override val parentMessage: ParentMessageItem?,
) : MessageItem

fun Message.toMessageItem(): MessageItem {
    return when (direction) {
        MessageDirection.OUTGOING -> OutgoingMessageItem(
            id = id,
            content = content,
            sentTime = creationTime,
            hasBeenRead = hasBeenRead(),
            readAt = readAt,
            reactions = reactions.toPersistentList(),
            parentMessage = parentMessage?.toItem(),
            status = status
        )
        MessageDirection.INCOMING -> IncomingMessageItem(
            id = id,
            content = content,
            sentTime = creationTime,
            hasBeenRead = hasBeenRead(),
            readAt = readAt,
            reactions = reactions.toPersistentList(),
            parentMessage = parentMessage?.toItem()
        )
    }
}

private fun Message.hasBeenRead(): Boolean {
    return when {
        this.readAt != null -> true
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

fun MessageItem.status(): MessageStatus? {
    return when (this) {
        is IncomingMessageItem -> null
        is OutgoingMessageItem -> this.status
    }
}
