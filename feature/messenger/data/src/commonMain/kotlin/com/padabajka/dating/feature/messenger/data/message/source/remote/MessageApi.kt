package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.domain.mapOfNotNull
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.SendMessageDto

interface MessageApi {
    suspend fun getMessages(params: GetParams): List<MessageDto>

    suspend fun postMessage(chatId: String, messageDto: SendMessageDto): MessageDto

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
    }
}
