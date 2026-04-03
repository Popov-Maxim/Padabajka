package com.padabajka.dating.feature.subscription.data.network

import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionStateResponse
import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.feature.subscription.data.source.model.PurchaseRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

class SubscriptionApi(
    private val ktorClientProvider: KtorClientProvider
) {

    /**
     * POST /subscription/purchase
     */
    suspend fun purchase(request: PurchaseRequest): SubscriptionStateResponse {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(PURCHASE_PATH)
            }

            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return response.body()
    }

    /**
     * GET /subscription/state
     */
    suspend fun getState(): SubscriptionStateResponse {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(GET_STATE_PATH)
            }
        }

        return response.body()
    }

    private companion object {
        private const val PURCHASE_PATH = "/subscription/purchase"
        private const val GET_STATE_PATH = "/subscription/state"
    }
}
