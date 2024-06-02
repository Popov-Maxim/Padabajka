package com.fp.padabajka.feature.messenger.data.message.source.local

import com.fp.padabajka.feature.messenger.data.message.model.MessageDto
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
