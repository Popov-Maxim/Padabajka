package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun messages(chatId: ChatId): Flow<List<Message>>
    fun lastMessage(chatId: ChatId): Flow<Message?>
    suspend fun sendMessage(chatId: ChatId, content: String, parentMessageId: MessageId? = null)
    suspend fun readMessage(messageId: MessageId)
    suspend fun reactToMessage(messageId: MessageId, reaction: MessageReaction.Value)
    suspend fun removeReactToMessage(messageId: MessageId)
}
