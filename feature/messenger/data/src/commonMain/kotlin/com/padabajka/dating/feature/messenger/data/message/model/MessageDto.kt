package com.padabajka.dating.feature.messenger.data.message.model

import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus

data class MessageDto(
    val id: String,
    val chatId: String,
    val authorId: String,
    val content: String,
    val creationTime: Long,
    val reaction: MessageReaction?,
    val reactionSynced: Boolean,
    val messageStatus: MessageStatus,
    val readSynced: Boolean,
    val parentMessageId: String?
)
