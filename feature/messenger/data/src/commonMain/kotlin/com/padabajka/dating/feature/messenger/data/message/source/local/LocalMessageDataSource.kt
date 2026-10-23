package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import kotlinx.coroutines.flow.Flow

interface LocalMessageDataSource {
    fun messages(chatId: String): Flow<List<MessageEntry>>
    fun lastMessage(chatId: String): Flow<MessageEntry?>
    suspend fun unreadMessagesCount(
        chatId: String,
        currentUserId: String,
        messageReadEvent: MessageReadEventEntry?
    ): Int
    suspend fun message(chatId: String, messageId: String): MessageEntry
    suspend fun messageOrNull(chatId: String, messageId: String): MessageEntry?
    suspend fun unsyncedEditMessageIds(chatId: String): List<String>
    suspend fun addMessage(message: MessageEntry)
    suspend fun deleteMessage(chatId: String, messageId: String)
    suspend fun deleteMessagesInChat(chatId: ChatId)
    suspend fun applyRemoteMessage(
        chatId: String,
        messageId: String,
        version: Long?,
        fromEvent: Boolean = false,
        update: (MessageEntry?) -> MessageEntry?,
    )
    suspend fun applyRemoteDeletion(
        chatId: String,
        messageId: String,
        version: Long?,
        fromEvent: Boolean = false,
    )
    suspend fun updateMessage(
        chatId: String,
        messageId: String,
        update: (MessageEntry) -> MessageEntry
    ): MessageEntry
    suspend fun markMessageFailedToSend(chatId: String, messageId: String)
}
