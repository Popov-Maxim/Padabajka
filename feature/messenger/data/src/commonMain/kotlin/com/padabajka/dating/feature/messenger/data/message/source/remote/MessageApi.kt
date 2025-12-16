package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.domain.mapOfNotNull
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageSyncResponse

interface MessageApi {
    suspend fun getMessages(params: GetParams): MessageSyncResponse
    suspend fun getSyncMessages(params: GetSyncParams): MessageSyncResponse

    suspend fun postMessage(messageDto: MessageRequest.Send): MessageDto.Existing

    suspend fun patchMessage(messageDto: MessageRequest.Edit): MessageDto.Existing

    suspend fun deleteMessage(chatId: String, messageId: String)

    suspend fun deleteChat(chatId: String)

    suspend fun markAsRead(messageRequest: MessageRequest.MarkAsRead)

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

        const val PATH_GET = "messages"
        const val PATH_GET_SYNC = "messages/sync"
        const val PATH_NEW = "new_message"
        const val PATH_PATCH = "patch_message"
        const val PATH_DELETE = "delete_message"
        const val PATH_DELETE_CHAT = "delete_chat"
        const val PATH_MARK_AS_READ = "mark_as_read"
    }
}
