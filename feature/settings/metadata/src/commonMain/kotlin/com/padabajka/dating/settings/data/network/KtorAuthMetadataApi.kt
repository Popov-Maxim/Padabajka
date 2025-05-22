package com.padabajka.dating.settings.data.network

import com.padabajka.dating.core.networking.KtorClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

internal class KtorAuthMetadataApi(
    private val ktorClientProvider: KtorClientProvider
) : AuthMetadataApi {
    override suspend fun patch(metadataDto: UpdateMetadataDto) {
        val client = ktorClientProvider.client()

        val response = client.patch {
            url {
                path(AuthMetadataApi.PATH)
            }

            contentType(ContentType.Application.Json)
            setBody(metadataDto)
        }

        return response.body()
    }

    override suspend fun delete(metadataDto: DeleteMetadataDto) {
        val client = ktorClientProvider.client()

        val response = client.delete {
            url {
                path(AuthMetadataApi.PATH)
            }

            contentType(ContentType.Application.Json)
            setBody(metadataDto)
        }

        return response.body()
    }
}
