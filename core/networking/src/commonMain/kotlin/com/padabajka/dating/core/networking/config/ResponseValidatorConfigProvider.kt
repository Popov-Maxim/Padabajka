package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.repository.api.exception.BadStatusCodeException
import com.padabajka.dating.core.repository.api.exception.ConnectException
import com.padabajka.dating.core.repository.api.exception.SuperLikeException
import com.padabajka.dating.core.repository.api.exception.UserException
import com.padabajka.dating.core.repository.api.exception.isConnectException
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable

private const val MIN_ERROR_STATUS_CODE = 400
private const val MAX_ERROR_STATUS_CODE = 599

class ResponseValidatorConfigProvider : KtorConfigProvider.Static {
    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            HttpResponseValidator {
                validateResponse {
                    val isWebSocket =
                        it.request.url.protocol in arrayOf(URLProtocol.WS, URLProtocol.WSS)
                    if (isWebSocket) return@validateResponse

                    val status = it.status
                    if (status.value in MIN_ERROR_STATUS_CODE..MAX_ERROR_STATUS_CODE) {
                        throwKnownApiError(it)
                    }
                    when (status) {
                        // TODO: server send Gone only for other person
                        HttpStatusCode.Gone -> throw UserException.Deleted()
                        HttpStatusCode.Forbidden -> throw UserException.Banned()
                        HttpStatusCode.Unauthorized -> throw UserException.Unauthorized()
                        else -> handleOtherStatus(status)
                    }
                }

                handleResponseExceptionWithRequest { cause, _ ->
                    if (
                        cause.isConnectException() ||
                        cause is HttpRequestTimeoutException
                    ) {
                        throw ConnectException(cause)
                    }

                    throw cause
                }
            }
        }

    private fun handleOtherStatus(status: HttpStatusCode) {
        if (status.value in MIN_ERROR_STATUS_CODE..MAX_ERROR_STATUS_CODE) {
            throw BadStatusCodeException(status.value, status.description)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun throwKnownApiError(response: HttpResponse) {
        val errorCode = try {
            response.body<ErrorResponse>().code
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            return
        }

        when (errorCode) {
            SuperLikeException.LIMIT_REACHED_CODE -> throw SuperLikeException.LimitReached()
            SuperLikeException.SUBSCRIPTION_REQUIRED_CODE -> throw SuperLikeException.SubscriptionRequired()
        }
    }
}

@Serializable
private data class ErrorResponse(
    val code: String,
    val message: String
)
