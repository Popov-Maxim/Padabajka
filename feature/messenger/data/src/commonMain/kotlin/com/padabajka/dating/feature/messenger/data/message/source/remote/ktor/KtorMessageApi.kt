package com.padabajka.dating.feature.messenger.data.message.source.remote.ktor

import com.padabajka.dating.core.data.utils.encodeToMap
import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.appendNotNull
import com.padabajka.dating.core.networking.utils.throwIfNotSuccessful
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageRequest
import com.padabajka.dating.feature.messenger.data.message.model.MessageSyncResponse
import com.padabajka.dating.feature.messenger.data.message.source.remote.api.MessageApi
import com.padabajka.dating.feature.messenger.data.message.source.remote.routes.MessageRoutes
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.serialization.json.Json

class KtorMessageApi(
    private val ktorClientProvider: KtorClientProvider
) : MessageApi {
    override suspend fun getMessages(
        chatId: ChatId,
        params: MessageRequest.Get
    ): MessageSyncResponse {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(MessageRoutes.messages(chatId))

                Json.encodeToMap(params).onEach { (key, value) ->
                    parameters.appendNotNull(key, value)
                }
            }
        }

        return response.body()
    }

    override suspend fun syncMessages(
        chatId: ChatId,
        params: MessageRequest.GetSync
    ): MessageSyncResponse {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(MessageRoutes.sync(chatId))

                Json.encodeToMap(params).onEach { (key, value) ->
                    parameters.appendNotNull(key, value)
                }
            }
        }

        return response.body()
    }

    override suspend fun sendMessage(
        chatId: ChatId,
        messageDto: MessageRequest.Send
    ): MessageDto.Existing {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(MessageRoutes.messages(chatId))
            }

            contentType(ContentType.Application.Json)
            setBody(messageDto)
        }

        return response.body()
    }

    override suspend fun editMessage(
        chatId: ChatId,
        messageId: MessageId,
        messageDto: MessageRequest.Edit
    ): MessageDto.Existing {
        val client = ktorClientProvider.client()

        val response = client.patch {
            url {
                path(MessageRoutes.message(chatId, messageId))
            }

            contentType(ContentType.Application.Json)
            setBody(messageDto)
        }

        return response.body()
    }

    override suspend fun deleteMessage(chatId: ChatId, messageId: MessageId) {
        val client = ktorClientProvider.client()

        val response = client.delete {
            url {
                path(MessageRoutes.message(chatId, messageId))
            }
        }

        return response.throwIfNotSuccessful()
    }
}
