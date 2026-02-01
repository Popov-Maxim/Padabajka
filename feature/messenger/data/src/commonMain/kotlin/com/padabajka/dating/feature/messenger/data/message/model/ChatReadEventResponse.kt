package com.padabajka.dating.feature.messenger.data.message.model

import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import com.padabajka.dating.component.room.messenger.entry.messageReadEventEntry
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import kotlinx.serialization.Serializable

@Serializable
class ChatReadEventResponse(
    val chatId: ChatId,
    val userId: PersonId,
    val lastReadMessageId: MessageId,
    val lastReadMessageTime: Long,
    val readAt: Long
)

fun ChatReadEventResponse.toEntity(): MessageReadEventEntry {
    return messageReadEventEntry(
        chatId = chatId.raw,
        userId = userId.raw,
        lastReadMessageId = lastReadMessageId.raw,
        lastReadMessageTime = lastReadMessageTime,
        readAt = readAt,
        readSynced = true
    )
}
