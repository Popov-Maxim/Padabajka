package com.fp.padabajka.core.repository.api.model.messenger

import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmInline

data class Message(
    val id: MessageId,
    val author: PersonId,
    val content: String,
    val hasBeenRead: Boolean,
    val sentTime: LocalDateTime,
    val reaction: MessageReaction?,
    val parentMessage: ParentMessage?
)

@JvmInline
value class MessageId(val id: Long)
