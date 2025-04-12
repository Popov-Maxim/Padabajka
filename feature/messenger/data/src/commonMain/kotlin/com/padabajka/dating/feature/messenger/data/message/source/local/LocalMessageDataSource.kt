package com.padabajka.dating.feature.messenger.data.message.source.local

import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import kotlinx.coroutines.flow.Flow

internal interface LocalMessageDataSource {
    fun messages(chatId: String): Flow<List<MessageDto>>
    suspend fun message(messageId: String): MessageDto
    suspend fun addMessage(message: MessageDto)
    suspend fun updateMessage(messageId: String, update: (MessageDto) -> MessageDto)
}
