package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.Message
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun messages(chatId: ChatId): Flow<List<Message>>
    suspend fun sendMessage(chatId: ChatId, content: String, parentMessageId: MessageId? = null)
    suspend fun readMessage(chatId: ChatId, messageId: MessageId)
    suspend fun reactToMessage(chatId: ChatId, messageId: MessageId, reaction: MessageReaction)
}
