package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.data.network.incoming.dto.MessageReactionResponse
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.data.message.model.ChatRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageSyncResponse
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.ChatApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.MessageApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.MessageReactionApi

internal class RemoteMessageDataSourceImpl(
    private val messageApi: MessageApi,
    private val chatApi: ChatApi,
    private val messageReactionApi: MessageReactionApi
) : RemoteMessageDataSource {
    override suspend fun sendMessage(chatId: ChatId, sendMessageDto: MessageRequest.Send): MessageDto.Existing {
        return messageApi.sendMessage(chatId, sendMessageDto)
    }

    override suspend fun deleteMessage(chatId: ChatId, messageId: MessageId) {
        messageApi.deleteMessage(chatId, messageId)
    }

    override suspend fun editMessage(
        chatId: ChatId,
        messageId: MessageId,
        editMessageDto: MessageRequest.Edit
    ): MessageDto.Existing {
        return messageApi.editMessage(chatId, messageId, editMessageDto)
    }

    override suspend fun sendReaction(
        chatId: ChatId,
        messageId: MessageId,
        request: MessageReactionRequest.Send
    ): MessageReactionResponse {
        return messageReactionApi.sendReaction(chatId, messageId, request)
    }

    override suspend fun removeReaction(
        chatId: ChatId,
        messageId: MessageId,
    ) {
        return messageReactionApi.removeReaction(chatId, messageId)
    }

    override suspend fun readMessages(chatId: ChatId, request: ChatRequest.MarkAsRead) {
        chatApi.markChatAsRead(chatId, request)
    }

    override suspend fun getMessages(chatId: ChatId, beforeMessageId: String?, count: Int): MessageSyncResponse {
        val params = MessageRequest.Get(beforeMessageId, count)
        return messageApi.getMessages(chatId, params)
    }

    override suspend fun getMessages(
        chatId: ChatId,
        fromEventNumber: Long,
        fromReadEventNumber: Long
    ): MessageSyncResponse {
        val params = MessageRequest.GetSync(fromEventNumber, fromReadEventNumber)
        return messageApi.syncMessages(chatId, params)
    }
}
