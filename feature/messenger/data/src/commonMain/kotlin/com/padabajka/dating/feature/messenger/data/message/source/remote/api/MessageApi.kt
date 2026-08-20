package com.padabajka.dating.feature.messenger.data.message.source.remote.api

import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageSyncResponse

interface MessageApi {
    /**
     * GET /chats/{chatId}/messages?beforeMessageId={beforeMessageId}&count={count}
     */
    suspend fun getMessages(chatId: ChatId, params: MessageRequest.Get): MessageSyncResponse

    /**
     * GET /chats/{chatId}/messages/sync?beforeMessageId={beforeMessageId}
     */
    suspend fun syncMessages(chatId: ChatId, params: MessageRequest.GetSync): MessageSyncResponse

    /**
     * POST /chats/{chatId}/messages
     */
    suspend fun sendMessage(chatId: ChatId, messageDto: MessageRequest.Send): MessageDto.Existing

    /**
     * PATCH /chats/{chatId}/messages/{messageId}
     */
    suspend fun editMessage(chatId: ChatId, messageId: MessageId, messageDto: MessageRequest.Edit): MessageDto.Existing

    /**
     * DELETE /chats/{chatId}/messages/{messageId}
     */
    suspend fun deleteMessage(chatId: ChatId, messageId: MessageId)
}
