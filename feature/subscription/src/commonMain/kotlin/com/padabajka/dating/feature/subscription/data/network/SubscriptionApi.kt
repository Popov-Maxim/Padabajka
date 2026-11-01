package com.padabajka.dating.feature.subscription.data.network

import com.padabajka.dating.core.data.network.incoming.dto.SubscriptionStateResponse
import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.handleResponseStatuses
import com.padabajka.dating.core.networking.utils.throwIfNotSuccessful
import com.padabajka.dating.core.repository.api.exception.SubscriptionAlreadyLinkedException
import com.padabajka.dating.core.repository.api.exception.SubscriptionException
import com.padabajka.dating.feature.subscription.data.source.model.PurchaseRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.path

class SubscriptionApi(
    private val ktorClientProvider: KtorClientProvider
) {

    /**
     * POST /subscription/purchase
     */
    suspend fun purchase(request: PurchaseRequest): SubscriptionStateResponse.Success {
        val client = ktorClientProvider.client()

        val response = client.post {
            handleResponseStatuses(
                HttpStatusCode.Conflict,
                HttpStatusCode.UnprocessableEntity,
                HttpStatusCode.NotFound
            )
            url {
                path(PURCHASE_PATH)
            }

            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return readSubscriptionState(response)
    }

    /**
     * GET /subscription/state
     */
    suspend fun getState(): SubscriptionStateResponse.Success {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(GET_STATE_PATH)
            }
        }

        return readSubscriptionState(response)
    }

    private suspend fun readSubscriptionState(response: HttpResponse): SubscriptionStateResponse.Success =
        when (val state = response.body<SubscriptionStateResponse>()) {
            is SubscriptionStateResponse.Success -> {
                response.throwIfNotSuccessful()
                state
            }
            is SubscriptionStateResponse.Failed -> throw state.toException()
        }

    private fun SubscriptionStateResponse.Failed.toException(): SubscriptionException = when (code) {
        "SUBSCRIPTION_ACCOUNT_MISMATCH" -> SubscriptionAlreadyLinkedException()
        "SUBSCRIPTION_ACCOUNT_ID_MISSING" -> SubscriptionException.ActivationFailed()
        "SUBSCRIPTION_INVALID_PURCHASE" -> SubscriptionException.InvalidPurchase()
        "SUBSCRIPTION_PURCHASE_NOT_FOUND" -> SubscriptionException.PurchaseNotFound()
        else -> SubscriptionException.ActivationFailed(
            cause = IllegalStateException("Unknown subscription response error code: $code")
        )
    }

    private companion object {
        private const val PURCHASE_PATH = "/subscription/purchase"
        private const val GET_STATE_PATH = "/subscription/state"
    }
}
