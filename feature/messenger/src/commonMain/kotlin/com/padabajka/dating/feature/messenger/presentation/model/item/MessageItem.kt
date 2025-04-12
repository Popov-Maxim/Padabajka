package com.padabajka.dating.feature.messenger.presentation.model.item

import androidx.compose.runtime.Immutable
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
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
