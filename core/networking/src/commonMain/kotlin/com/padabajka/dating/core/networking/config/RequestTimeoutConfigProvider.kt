package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.utils.isDebugBuild
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RequestTimeoutConfigProvider : KtorConfigProvider.Static {

    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(HttpTimeout) {
                // TODO(P1): check valid
                val duration = if (isDebugBuild) Duration.INFINITE else 20.seconds
                requestTimeoutMillis = duration.inWholeMilliseconds
            }
        }
}
