package com.fp.padabajka.feature.messenger.data.message.source.local

import com.fp.padabajka.feature.messenger.data.message.model.MessageDto
import kotlinx.coroutines.flow.Flow

interface LocalMessageDataSource {
    fun messages(chatId: String): Flow<List<MessageDto>>
    suspend fun addMessage(message: MessageDto): MessageDto
    suspend fun updateMessage(chatId: String, messageId: Long, update: (MessageDto) -> MessageDto)
    suspend fun message(chatId: String, messageId: Long): MessageDto
}
