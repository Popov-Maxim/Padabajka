package com.padabajka.dating.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import kotlin.time.Duration

class RequestTimeoutConfigProvider : KtorConfigProvider.Static {

    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(HttpTimeout) {
                requestTimeoutMillis = TIMEOUT
            }
        }

    private companion object {
        private val TIMEOUT = Duration.INFINITE.inWholeMilliseconds // TODO: change for prod
    }
}
