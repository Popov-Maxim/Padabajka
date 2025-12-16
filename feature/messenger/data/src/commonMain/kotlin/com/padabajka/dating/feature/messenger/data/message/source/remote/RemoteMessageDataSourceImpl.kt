package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageSyncResponse

internal class RemoteMessageDataSourceImpl(
    private val messageApi: MessageApi
) : RemoteMessageDataSource {
    override suspend fun sendMessage(sendMessageDto: MessageRequest.Send): MessageDto.Existing {
        return messageApi.postMessage(sendMessageDto)
    }

    override suspend fun deleteMessage(chatId: String, messageId: String) {
        messageApi.deleteMessage(chatId, messageId)
    }

    override suspend fun editMessage(
        editMessageDto: MessageRequest.Edit
    ): MessageDto.Existing {
        return messageApi.patchMessage(editMessageDto)
    }

    override suspend fun sendReaction(messageId: String, reaction: MessageReactionDto): MessageReactionDto {
        TODO("Not yet implemented")
    }

    override suspend fun removeReaction(messageId: String, author: String) {
        TODO("Not yet implemented")
    }

    override suspend fun readMessages(messageRequest: MessageRequest.MarkAsRead) {
        messageApi.markAsRead(messageRequest)
    }

    override suspend fun getMessages(chatId: String, beforeMessageId: String?, count: Int): MessageSyncResponse {
        val params = MessageApi.GetParams(chatId, beforeMessageId, count)
        return messageApi.getMessages(params)
    }

    override suspend fun getMessages(
        chatId: String,
        fromEventNumber: Long
    ): MessageSyncResponse {
        val params = MessageApi.GetSyncParams(chatId, fromEventNumber)
        return messageApi.getSyncMessages(params)
    }
}
