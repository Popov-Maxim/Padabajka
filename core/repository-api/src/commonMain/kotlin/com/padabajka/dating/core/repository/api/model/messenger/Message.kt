package com.padabajka.dating.core.repository.api.model.messenger

import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

data class Message(
    val id: MessageId,
    val direction: MessageDirection,
    val content: String,
    val creationTime: LocalDateTime,
    val status: MessageStatus,
    val readAt: LocalDateTime?,
    val reactions: List<MessageReaction>,
    val parentMessage: ParentMessage?,
    val editedAt: Long?,
)

@Serializable
data class RawMessage(
    val id: MessageId,
    val authorId: PersonId,
    val content: String,
    val creationTime: Long,
    val parentMessageId: MessageId?,
    val editedAt: Long?,
)

@JvmInline
@Serializable
value class MessageId(val raw: String)
