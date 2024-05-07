package com.fp.padabajka.feature.messenger.data.model

import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.core.repository.api.model.messenger.MessageStatus

data class MessageDto(
    val id: Long?,
    val matchId: String,
    val isIncoming: Boolean,
    val content: String,
    val creationTime: Long,
    val reaction: MessageReaction?,
    val reactionSynced: Boolean,
    val messageStatus: MessageStatus,
    val readSynced: Boolean,
    val parentMessageId: Long?
)
