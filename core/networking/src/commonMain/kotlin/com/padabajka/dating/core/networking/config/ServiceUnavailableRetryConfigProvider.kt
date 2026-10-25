package com.padabajka.dating.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.http.HttpStatusCode

internal class ServiceUnavailableRetryConfigProvider : KtorConfigProvider.Static {
    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(HttpRequestRetry) {
                retryIf { _, response ->
                    response.status == HttpStatusCode.ServiceUnavailable ||
                        response.status == HttpStatusCode.BadGateway
                }
                retryOnExceptionIf { _, _ ->
                    false
                }
            }
        }
}
