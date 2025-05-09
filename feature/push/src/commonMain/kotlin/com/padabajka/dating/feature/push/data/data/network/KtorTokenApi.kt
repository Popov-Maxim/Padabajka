package com.padabajka.dating.feature.push.data.data.network

import com.padabajka.dating.core.networking.KtorClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

class KtorTokenApi(
    private val ktorClientProvider: KtorClientProvider
) : TokenApi {
    override suspend fun post(data: TokenApi.BodyData) {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(TokenApi.PATH)
            }

            contentType(ContentType.Application.Json)
            setBody(data)
        }

        return response.body()
    }
}
