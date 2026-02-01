package com.padabajka.dating.feature.messenger.data.message.source.remote.api

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.throwIfNotSuccessful
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.feature.messenger.data.message.model.ChatRequest
import com.padabajka.dating.feature.messenger.data.message.source.remote.routes.ChatRoutes
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

class MessageReadEventApi(
    private val ktorClientProvider: KtorClientProvider
) {
    /**
     * POST /chats/{chatId}/read
     */
    suspend fun markChatAsRead(
        chatId: ChatId,
        request: ChatRequest.MarkAsRead
    ) {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(ChatRoutes.markAsRead(chatId))
            }

            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return response.throwIfNotSuccessful()
    }
}
