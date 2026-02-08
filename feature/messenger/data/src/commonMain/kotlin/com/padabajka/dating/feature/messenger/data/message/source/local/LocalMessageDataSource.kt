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
    suspend fun message(messageId: String): MessageEntry
    suspend fun addMessage(message: MessageEntry)
    suspend fun deleteMessage(messageId: String)
    suspend fun deleteMessagesInChat(chatId: ChatId)
    suspend fun updateMessages(messagesForAdd: List<MessageEntry>, messageIdsForDelete: List<String>)
    suspend fun updateMessage(messageId: String, update: (MessageEntry) -> MessageEntry): MessageEntry
}
