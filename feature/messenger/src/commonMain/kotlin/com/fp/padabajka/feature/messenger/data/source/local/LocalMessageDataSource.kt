package com.fp.padabajka.feature.messenger.data.source.local

import com.fp.padabajka.feature.messenger.data.model.MessageDto
import kotlinx.coroutines.flow.Flow

interface LocalMessageDataSource {
    fun messages(matchId: String): Flow<List<MessageDto>>
    suspend fun addMessage(message: MessageDto): MessageDto
    suspend fun updateMessage(matchId: String, messageId: Long, update: (MessageDto) -> MessageDto)
    suspend fun message(matchId: String, messageId: Long): MessageDto
}
