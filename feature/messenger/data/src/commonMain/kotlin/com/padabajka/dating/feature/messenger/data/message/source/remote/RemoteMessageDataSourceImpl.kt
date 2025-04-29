package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto

internal class RemoteMessageDataSourceImpl : RemoteMessageDataSource {
    override suspend fun sendMessage(chatId: String, content: String): MessageDto {
        TODO("Not yet implemented")
    }

    override suspend fun sendReaction(messageId: String, reaction: MessageReactionDto): MessageReactionDto {
        TODO("Not yet implemented")
    }

    override suspend fun removeReaction(messageId: String, author: String) {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(messageId: String) {
        TODO("Not yet implemented")
    }
}
