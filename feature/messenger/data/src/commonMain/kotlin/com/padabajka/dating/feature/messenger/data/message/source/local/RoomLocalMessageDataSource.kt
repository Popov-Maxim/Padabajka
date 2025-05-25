package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.MessageDao
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import kotlinx.coroutines.flow.Flow

internal class RoomLocalMessageDataSource(private val messageDao: MessageDao) :
    LocalMessageDataSource {

    override fun messages(chatId: String): Flow<List<MessageEntry>> =
        messageDao.messagesByChatId(chatId)

    override fun lastMessage(chatId: String): Flow<MessageEntry?> {
        return messageDao.lastMessageByChatId(chatId)
    }

    override suspend fun message(messageId: String): MessageEntry {
        return messageDao.messageById(messageId)
    }

    override suspend fun addMessage(message: MessageEntry) {
        messageDao.insertMessage(message)
    }

    override suspend fun addMessages(messages: List<MessageEntry>) {
        messageDao.insertMessages(messages)
    }

    override suspend fun updateMessage(
        messageId: String,
        update: (MessageEntry) -> MessageEntry
    ) {
        val old = messageDao.messageById(messageId)
        val updated = update(old)

        if (old.id != updated.id) {
            messageDao.replaceMessage(old = old, new = updated)
        }

        messageDao.updateMessage(updated)
    }
}
