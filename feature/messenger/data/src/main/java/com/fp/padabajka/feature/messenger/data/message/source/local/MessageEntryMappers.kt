package com.fp.padabajka.feature.messenger.data.message.source.local

import com.fp.padabajka.feature.messenger.data.message.model.MessageDto
import com.fp.padabajka.feature.messenger.data.message.room.MessageEntry

internal fun MessageEntry.toDto(): MessageDto {
    return MessageDto(
        id = id,
        chatId = chatId,
        authorId = authorId,
        content = content,
        creationTime = creationTime,
        reaction = reaction,
        reactionSynced = reactionSynced,
        messageStatus = messageStatus,
        readSynced = readSynced,
        parentMessageId = parentMessageId
    )
}

internal fun MessageDto.toEntry(): MessageEntry {
    return MessageEntry(
        id = id,
        chatId = chatId,
        authorId = authorId,
        content = content,
        creationTime = creationTime,
        reaction = reaction,
        reactionSynced = reactionSynced,
        messageStatus = messageStatus,
        readSynced = readSynced,
        parentMessageId = parentMessageId
    )
}
