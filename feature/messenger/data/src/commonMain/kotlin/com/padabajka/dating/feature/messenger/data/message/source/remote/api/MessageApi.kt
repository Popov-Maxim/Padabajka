package com.padabajka.dating.feature.messenger.data.message.source.remote.api

import com.padabajka.dating.core.domain.mapOfNotNull
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageSyncResponse

interface MessageApi {
    /**
     * GET /messages?chatId={chatId}&beforeMessageId={beforeMessageId}&count={count}
     */
    suspend fun getMessages(params: GetParams): MessageSyncResponse

    /**
     * GET /messages/sync?chatId={chatId}&beforeMessageId={beforeMessageId}
     */
    suspend fun syncMessages(params: GetSyncParams): MessageSyncResponse

    /**
     * POST /messages
     */
    suspend fun sendMessage(messageDto: MessageRequest.Send): MessageDto.Existing

    /**
     * PATCH /messages
     */
    suspend fun editMessage(messageDto: MessageRequest.Edit): MessageDto.Existing

    /**
     * DELETE /chats/{chatId}/messages/{messageId}
     */
    suspend fun deleteMessage(chatId: ChatId, messageId: MessageId)

    data class GetParams(
        val chatId: String,
        val beforeMessageId: String?,
        val count: Int
    ) {
        fun toMap(): Map<String, String> {
            return mapOfNotNull(
                CHAT_ID to chatId,
                BEFORE_MESSAGE_ID to beforeMessageId,
                COUNT to count.toString()
            )
        }

        companion object {
            private const val BEFORE_MESSAGE_ID = "beforeMessageId"
            private const val COUNT = "count"
        }
    }

    data class GetSyncParams(
        val chatId: String,
        val lastEventNumber: Long
    ) {
        fun toMap(): Map<String, String> {
            return mapOfNotNull(
                CHAT_ID to chatId,
                LAST_EVENT_NUMBER to lastEventNumber.toString(),
            )
        }

        companion object {
            private const val LAST_EVENT_NUMBER = "lastEventNumber"
        }
    }

    companion object {
        private const val CHAT_ID = "chatId"
    }
}
