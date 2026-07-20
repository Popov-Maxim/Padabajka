package com.padabajka.dating.feature.legal.data.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.UnauthKtorClientProvider
import com.padabajka.dating.core.networking.utils.throwIfNotSuccessful
import com.padabajka.dating.feature.legal.data.network.model.LegalAcceptRequest
import com.padabajka.dating.feature.legal.data.network.model.LegalVersionsResponse
import com.padabajka.dating.feature.legal.data.network.model.MeLegalVersionsResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

class LegalApi(
    private val ktorClientProvider: KtorClientProvider,
    private val unauthKtorClientProvider: UnauthKtorClientProvider
) {

    suspend fun postAccepted(request: LegalAcceptRequest) {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path("legal/accept")
            }

            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return response.throwIfNotSuccessful()
    }

    suspend fun getActual(): LegalVersionsResponse {
        val client = unauthKtorClientProvider.client()

        val response = client.get {
            url {
                path("legal/version")
            }
        }

        return response.body()
    }

    suspend fun getAccept(): MeLegalVersionsResponse {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path("legal/version/me")
            }
        }

        return response.body()
    }
}
