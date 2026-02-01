package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.MessageReadEventDao
import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import com.padabajka.dating.core.repository.api.model.auth.UserId
import kotlinx.coroutines.flow.Flow

class LocalChatReadStateDataSourceImpl(
    private val messageReadEventDao: MessageReadEventDao
) : LocalChatReadStateDataSource {
    override fun readStates(chatId: String): Flow<List<MessageReadEventEntry>> {
        return messageReadEventDao.readStatesByChatId(chatId)
    }

    override suspend fun addMessageReadEvent(messageReadEvent: MessageReadEventEntry): MessageReadEventEntry {
        val id = messageReadEventDao.insert(messageReadEvent)
        return messageReadEvent.copy(id = id)
    }

    override suspend fun updateMessageReadEvent(
        eventId: Long,
        update: (MessageReadEventEntry) -> MessageReadEventEntry
    ) {
        val current = messageReadEventDao.getById(eventId)
            ?: return

        val updated = update(current)

        if (updated.id != current.id) {
            error("eventId must not be changed")
        }

        messageReadEventDao.update(updated)
    }

    override suspend fun addMessageReadEvents(events: List<MessageReadEventEntry>) {
        messageReadEventDao.insert(events)
    }

    override suspend fun lastReadEvent(chatId: String, userId: UserId): MessageReadEventEntry? {
        return messageReadEventDao.lastReadEvent(chatId, userId.raw)
    }
}
