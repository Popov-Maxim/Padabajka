package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.feature.messenger.data.message.model.EditMessageDto
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.SendMessageDto
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.path

class KtorMessageApi(
    private val ktorClientProvider: KtorClientProvider
) : MessageApi {
    override suspend fun getMessages(params: MessageApi.GetParams): List<MessageDto> {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(MessageApi.PATH_GET)

                params.toMap().onEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
        }

        return response.body()
    }

    override suspend fun postMessage(
        messageDto: SendMessageDto
    ): MessageDto.Existing {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(MessageApi.PATH_NEW)
            }

            contentType(ContentType.Application.Json)
            setBody(messageDto)
        }

        return response.body()
    }

    override suspend fun patchMessage(messageDto: EditMessageDto): MessageDto.Existing {
        val client = ktorClientProvider.client()

        val response = client.patch {
            url {
                path(MessageApi.PATH_PATCH)
            }

            contentType(ContentType.Application.Json)
            setBody(messageDto)
        }

        return response.body()
    }

    override suspend fun deleteMessage(chatId: String, messageId: String) {
        val client = ktorClientProvider.client()

        val response = client.delete {
            url {
                path(MessageApi.PATH_DELETE + "/$chatId/$messageId")
            }
        }

        return response.throwIfNotSuccessful()
    }

    private fun HttpResponse.throwIfNotSuccessful() {
        if (!status.isSuccess()) {
            throw ResponseException(this, "HTTP error: $status")
        }
    }
}
