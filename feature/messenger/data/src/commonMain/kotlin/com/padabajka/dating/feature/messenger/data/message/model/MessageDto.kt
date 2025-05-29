package com.padabajka.dating.feature.messenger.data.message.model

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import com.padabajka.dating.core.repository.api.model.messenger.RawMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageDto {
    val id: String
    val chatId: String
    val creationTime: Long

    @Serializable
    @SerialName("deleted")
    data class Deleted(
        override val id: String,
        override val chatId: String,
        override val creationTime: Long
    ) : MessageDto

    @Serializable
    @SerialName("existing")
    data class Existing(
        override val id: String,
        override val chatId: String,
        override val creationTime: Long,
        val editedAt: Long?,
        val authorId: String,
        val content: String,
        val reactions: List<MessageReactionDto>,
        val readAt: Long?,
        val parentMessageId: String?
    ) : MessageDto
}

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

fun MessageDto.Existing.toEntity(): MessageEntry {
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

fun RawMessage.toEntity(chatId: ChatId): MessageEntry {
    return MessageEntry(
        id = id.raw,
        chatId = chatId.raw,
        authorId = authorId.raw,
        content = content,
        creationTime = creationTime,
        reactions = listOf(),
        messageStatus = MessageStatus.Sent,
        readAt = null,
        readSynced = true,
        parentMessageId = parentMessageId?.raw
    )
}
