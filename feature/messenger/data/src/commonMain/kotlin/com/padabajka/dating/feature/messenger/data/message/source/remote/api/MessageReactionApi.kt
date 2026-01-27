package com.padabajka.dating.feature.messenger.data.message.source.remote.api

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageReactionRequest
import com.padabajka.dating.feature.messenger.data.message.source.remote.routes.MessageReactionRoutes
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

class MessageReactionApi(
    private val ktorClientProvider: KtorClientProvider
) {

    /**
     * POST /chats/{chatId}/messages/{messageId}/reactions
     */
    suspend fun sendReaction(
        chatId: ChatId,
        messageId: MessageId,
        request: MessageReactionRequest.Send
    ): MessageReactionDto {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(MessageReactionRoutes.reactions(chatId, messageId))

                contentType(ContentType.Application.Json)
                setBody(request)
            }
        }

        return response.body()
    }

    /**
     * DELETE /chats/{chatId}/messages/{messageId}/reactions
     */
    suspend fun removeReaction(
        chatId: ChatId,
        messageId: MessageId,
    ) {
        val client = ktorClientProvider.client()

        val response = client.delete {
            url {
                path(MessageReactionRoutes.reactions(chatId, messageId))
            }
        }

        return response.body()
    }
}
