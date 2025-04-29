package com.padabajka.dating.feature.messenger.data.message.model

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.feature.messenger.data.message.source.local.toEntity

data class MessageDto(
    val id: String,
    val chatId: String,
    val authorId: String,
    val content: String,
    val creationTime: Long,
    val reactions: List<MessageReaction>,
    val messageStatus: MessageStatus,
    val readAt: Long?,
    val readSynced: Boolean,
    val parentMessageId: String?
)

fun MessageDto.toEntity(): MessageEntry {
    return MessageEntry(
        id = id,
        chatId = chatId,
        authorId = authorId,
        content = content,
        creationTime = creationTime,
        reactions = reactions.map { it.toEntity() },
        messageStatus = messageStatus,
        readAt = readAt,
        readSynced = readSynced,
        parentMessageId = parentMessageId
    )
}
