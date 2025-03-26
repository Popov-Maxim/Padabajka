package com.padabajka.dating.feature.messenger.data.source.local

import com.padabajka.dating.feature.messenger.data.model.MessageDto
import kotlinx.coroutines.flow.Flow

class LocalMessageDataSourceImpl : LocalMessageDataSource {

    override fun messages(chatId: String): Flow<List<MessageDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMessage(message: MessageDto): MessageDto {
        TODO("Not yet implemented")
    }

    override suspend fun updateMessage(
        chatId: String,
        messageId: Long,
        update: (MessageDto) -> MessageDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun message(chatId: String, messageId: Long): MessageDto {
        TODO("Not yet implemented")
    }
}
