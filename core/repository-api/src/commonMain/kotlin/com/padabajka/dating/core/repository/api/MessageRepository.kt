package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun messages(chatId: ChatId): Flow<List<Message>>
    fun lastMessage(chatId: ChatId): Flow<Message?>
    suspend fun unreadMessagesCount(chatId: ChatId): Int
    suspend fun sendMessage(chatId: ChatId, content: String, parentMessageId: MessageId? = null)
    suspend fun deleteMessage(chatId: ChatId, messageId: MessageId)
    suspend fun deleteLocalMessage(chatId: ChatId, messageId: MessageId)
    suspend fun editMessage(chatId: ChatId, messageId: MessageId, content: String)

    suspend fun readMessage(messageId: MessageId)
    suspend fun reactToMessage(messageId: MessageId, reaction: MessageReaction.Value)
    suspend fun removeReactToMessage(messageId: MessageId)
    suspend fun messageReactions(messageId: MessageId): List<MessageReaction>

    suspend fun loadMessages(chatId: ChatId, beforeMessageId: MessageId, count: Int)
    suspend fun loadMessages(chatId: ChatId, count: Int): SyncResult
    suspend fun syncMessages(chatId: ChatId, lastEventNumber: Long, lastReadEventNumber: Long): SyncResult
}

data class SyncResult(
    val lastEventNumber: Long,
    val lastReadEventNumber: Long
)
