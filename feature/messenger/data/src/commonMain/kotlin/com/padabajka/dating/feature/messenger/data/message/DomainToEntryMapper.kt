package com.padabajka.dating.feature.messenger.data.message

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.RawMessage
import com.padabajka.dating.core.repository.api.model.swiper.PersonId

fun MessageEntry.toRawMessage(): RawMessage {
    return RawMessage(
        id = id.run(::MessageId),
        authorId = authorId.run(::PersonId),
        content = content,
        creationTime = creationTime,
        parentMessageId = parentMessageId?.run(::MessageId),
        editedAt = editedAt,
        readAt = readAt
    )
}

fun MessageEntry.merge(message: RawMessage): MessageEntry {
    return copy(
        authorId = message.authorId.raw,
        content = message.content,
        editedAt = message.editedAt,
        readAt = message.readAt,
        parentMessageId = message.parentMessageId?.raw,
        creationTime = message.creationTime
    )
}
