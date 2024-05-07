package com.fp.padabajka.feature.messenger.data.source.local

import com.fp.padabajka.feature.messenger.data.model.MessageDto
import kotlinx.coroutines.flow.Flow

class LocalMessageDataSourceImpl : LocalMessageDataSource {

    override fun messages(matchId: String): Flow<List<MessageDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMessage(message: MessageDto): MessageDto {
        TODO("Not yet implemented")
    }

    override suspend fun updateMessage(
        matchId: String,
        messageId: Long,
        update: (MessageDto) -> MessageDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun message(matchId: String, messageId: Long): MessageDto {
        TODO("Not yet implemented")
    }
}
