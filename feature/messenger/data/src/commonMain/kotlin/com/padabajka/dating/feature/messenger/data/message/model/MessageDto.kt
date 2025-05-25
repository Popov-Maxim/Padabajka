package com.padabajka.dating.feature.messenger.data.message.model

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val chatId: String,
    val authorId: String,
    val content: String,
    val creationTime: Long,
    val reactions: List<MessageReactionDto>,
    val readAt: Long?,
    val parentMessageId: String?
)

@Serializable
data class SendMessageDto(
    val id: String,
    val chatId: String,
    val content: String,
    val parentMessageId: String?
)

fun MessageEntry.toSendDto(): SendMessageDto {
    return SendMessageDto(
        id = id,
        chatId = chatId,
        content = content,
        parentMessageId = parentMessageId
    )
}

fun MessageDto.toEntity(): MessageEntry {
    return MessageEntry(
        id = id,
        chatId = chatId,
        authorId = authorId,
        content = content,
        creationTime = creationTime,
        reactions = reactions.map { it.toEntity() },
        messageStatus = MessageStatus.Sent,
        readAt = readAt,
        readSynced = true,
        parentMessageId = parentMessageId
    )
}
