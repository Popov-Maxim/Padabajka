package com.fp.padabajka.feature.messenger.data.message.source.local

import com.fp.padabajka.component.room.messenger.entry.MessageEntry
import com.fp.padabajka.feature.messenger.data.message.model.MessageDto

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
