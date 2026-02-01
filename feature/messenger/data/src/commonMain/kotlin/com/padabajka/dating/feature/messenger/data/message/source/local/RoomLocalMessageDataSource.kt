package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.MessageDao
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import kotlinx.coroutines.flow.Flow

internal class RoomLocalMessageDataSource(private val messageDao: MessageDao) :
    LocalMessageDataSource {

    override fun messages(chatId: String): Flow<List<MessageEntry>> =
        messageDao.messagesByChatId(chatId)

    override fun lastMessage(chatId: String): Flow<MessageEntry?> {
        return messageDao.lastMessageByChatId(chatId)
    }

    override suspend fun unreadMessagesCount(
        chatId: String,
        currentUserId: String,
        messageReadEvent: MessageReadEventEntry?
    ): Int {
        return messageDao.unreadMessagesCount(
            chatId,
            currentUserId,
            messageReadEvent?.lastReadMessageTime ?: 0
        )
    }

    override suspend fun message(messageId: String): MessageEntry {
        return messageDao.messageById(messageId)
    }

    override suspend fun addMessage(message: MessageEntry) {
        messageDao.insertMessage(message)
    }

    override suspend fun deleteMessage(messageId: String) {
        messageDao.deleteMessageById(messageId)
    }

    override suspend fun updateMessages(
        messagesForAdd: List<MessageEntry>,
        messageIdsForDelete: List<String>
    ) {
        messageDao.updateMessages(messagesForAdd, messageIdsForDelete)
    }

    override suspend fun updateMessage(
        messageId: String,
        update: (MessageEntry) -> MessageEntry
    ): MessageEntry {
        val old = messageDao.messageById(messageId)
        val updated = update(old)

        if (old.id != updated.id) {
            messageDao.replaceMessage(old = old, new = updated)
        }

        messageDao.updateMessage(updated)
        return updated
    }
}
