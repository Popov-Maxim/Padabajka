package com.padabajka.dating.feature.messenger.data.source.local

import com.padabajka.dating.feature.messenger.data.model.MessageDto
import kotlinx.coroutines.flow.Flow

interface LocalMessageDataSource {
    fun messages(chatId: String): Flow<List<MessageDto>>
    suspend fun addMessage(message: MessageDto): MessageDto
    suspend fun updateMessage(chatId: String, messageId: Long, update: (MessageDto) -> MessageDto)
    suspend fun message(chatId: String, messageId: Long): MessageDto
}
