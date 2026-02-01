package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import com.padabajka.dating.core.repository.api.model.auth.UserId
import kotlinx.coroutines.flow.Flow

interface LocalChatReadStateDataSource {
    fun readStates(chatId: String): Flow<List<MessageReadEventEntry>>
    suspend fun lastReadEvent(chatId: String, userId: UserId): MessageReadEventEntry?

    suspend fun addMessageReadEvent(messageReadEvent: MessageReadEventEntry): MessageReadEventEntry
    suspend fun addMessageReadEvents(events: List<MessageReadEventEntry>)
    suspend fun updateMessageReadEvent(
        eventId: Long,
        update: (MessageReadEventEntry) -> MessageReadEventEntry
    )
}
