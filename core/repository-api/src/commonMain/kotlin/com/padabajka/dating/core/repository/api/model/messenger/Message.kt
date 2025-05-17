package com.padabajka.dating.core.repository.api.model.messenger

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
    val parentMessage: ParentMessage?
)

@JvmInline
@Serializable
value class MessageId(val raw: String)
