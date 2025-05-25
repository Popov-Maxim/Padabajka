package com.padabajka.dating.feature.messenger.data.message.source.remote

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.feature.messenger.data.message.model.MessageDto
import com.padabajka.dating.feature.messenger.data.message.model.SendMessageDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    override suspend fun postMessage(chatId: String, messageDto: SendMessageDto): MessageDto {
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
}
