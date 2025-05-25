package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto
import com.padabajka.dating.feature.messenger.data.message.model.SendMessageDto

internal class RemoteMessageDataSourceImpl(
    private val messageApi: MessageApi
) : RemoteMessageDataSource {
    override suspend fun sendMessage(chatId: String, sendMessageDto: SendMessageDto): MessageDto {
        return messageApi.postMessage(chatId, sendMessageDto)
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

    override suspend fun getMessages(chatId: String, beforeMessageId: String?, count: Int): List<MessageDto> {
        val params = MessageApi.GetParams(chatId, beforeMessageId, count)
        return messageApi.getMessages(params)
    }
}
