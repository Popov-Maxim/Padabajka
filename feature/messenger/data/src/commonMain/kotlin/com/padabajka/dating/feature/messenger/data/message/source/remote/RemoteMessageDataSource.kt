package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.data.message.model.ChatRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageSyncResponse

internal interface RemoteMessageDataSource {
    suspend fun sendMessage(sendMessageDto: MessageRequest.Send): MessageDto.Existing
    suspend fun deleteMessage(chatId: ChatId, messageId: MessageId)
    suspend fun editMessage(editMessageDto: MessageRequest.Edit): MessageDto.Existing
    suspend fun sendReaction(
        chatId: ChatId,
        messageId: MessageId,
        request: MessageReactionRequest.Send
    ): MessageReactionDto

    suspend fun removeReaction(
        chatId: ChatId,
        messageId: MessageId,
    )

    suspend fun readMessages(chatId: ChatId, request: ChatRequest.MarkAsRead)
    suspend fun getMessages(
        chatId: String,
        beforeMessageId: String?,
        count: Int
    ): MessageSyncResponse

    suspend fun getMessages(chatId: String, fromEventNumber: Long): MessageSyncResponse
}
