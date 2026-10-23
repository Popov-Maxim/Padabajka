package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.MessageDao
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
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

    override suspend fun message(chatId: String, messageId: String): MessageEntry {
        return messageDao.messageById(chatId, messageId)
    }

    override suspend fun messageOrNull(chatId: String, messageId: String): MessageEntry? {
        return messageDao.messageByIdOrNull(chatId, messageId)
    }

    override suspend fun unsyncedEditMessageIds(chatId: String): List<String> =
        messageDao.unsyncedEditMessageIds(chatId)

    override suspend fun addMessage(message: MessageEntry) {
        messageDao.insertMessage(message)
    }

    override suspend fun oldestMessageId(chatId: ChatId): MessageId? {
        return messageDao.oldestMessageByChatId(chatId.raw)?.run(::MessageId)
    }

    override suspend fun deleteMessage(chatId: String, messageId: String) {
        messageDao.deleteMessageById(chatId, messageId)
    }

    override suspend fun deleteMessagesInChat(chatId: ChatId) {
        messageDao.deleteMessagesByChatId(chatId.raw)
    }

    override suspend fun applyRemoteMessage(
        chatId: String,
        messageId: String,
        version: Long?,
        fromEvent: Boolean,
        update: (MessageEntry?) -> MessageEntry?,
    ) = messageDao.applyRemoteChange(
        chatId = chatId,
        messageId = messageId,
        version = version,
        deleted = false,
        fromEvent = fromEvent,
        update = update,
    )

    override suspend fun applyRemoteDeletion(
        chatId: String,
        messageId: String,
        version: Long?,
        fromEvent: Boolean,
    ) = messageDao.applyRemoteChange(
        chatId = chatId,
        messageId = messageId,
        version = version,
        deleted = true,
        fromEvent = fromEvent,
        update = { null },
    )

    override suspend fun updateMessage(
        chatId: String,
        messageId: String,
        update: (MessageEntry) -> MessageEntry
    ): MessageEntry = messageDao.updateMessage(chatId, messageId, update)

    override suspend fun markMessageFailedToSend(chatId: String, messageId: String) {
        messageDao.updateMessageStatusUnless(
            chatId = chatId,
            id = messageId,
            status = MessageStatus.FailedToSend,
            excludedStatus = MessageStatus.Sent,
        )
    }
}
