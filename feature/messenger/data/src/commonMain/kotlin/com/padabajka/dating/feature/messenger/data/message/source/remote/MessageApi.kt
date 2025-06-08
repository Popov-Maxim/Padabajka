package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.domain.mapOfNotNull
import com.padabajka.dating.feature.messenger.data.message.model.EditMessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.SendMessageDto

interface MessageApi {
    suspend fun getMessages(params: GetParams): List<MessageDto>

    suspend fun postMessage(messageDto: SendMessageDto): MessageDto.Existing

    suspend fun patchMessage(messageDto: EditMessageDto): MessageDto.Existing

    suspend fun deleteMessage(chatId: String, messageId: String)

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
            private const val CHAT_ID = "chatId"
            private const val BEFORE_MESSAGE_ID = "beforeMessageId"
            private const val COUNT = "count"
        }
    }

    companion object {
        const val PATH_GET = "messages"
        const val PATH_NEW = "new_message"
        const val PATH_PATCH = "patch_message"
        const val PATH_DELETE = "delete_message"
    }
}
