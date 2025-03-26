package com.padabajka.dating.feature.image.data.network

import com.padabajka.dating.core.data.network.model.ImageDto
import com.padabajka.dating.core.networking.KtorClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.path

class KtorImageApi(
    private val ktorClientProvider: KtorClientProvider
) : ImageApi {
    override suspend fun post(byteArray: ByteArray): ImageDto {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(ImageApi.PATH)
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", byteArray, headers())
                    }
                )
            )
        }

        return response.body()
    }

    private fun headers(): Headers {
        return Headers.build {
            append(HttpHeaders.ContentType, "image/jpeg")
            append(HttpHeaders.ContentDisposition, "filename=\"new_image\"")
        }
    }
}
