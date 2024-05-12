package com.fp.padabajka.feature.messenger.data.source.remote

import com.fp.padabajka.feature.messenger.data.model.MessageDto

class RemoteMessageDataSourceImpl : RemoteMessageDataSource {
    override suspend fun sendMessage(chatId: String, content: String): MessageDto {
        TODO("Not yet implemented")
    }

    override suspend fun sendReaction(chatId: String, messageId: Long, reaction: String) {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(chatId: String, messageId: Long) {
        TODO("Not yet implemented")
    }
}
