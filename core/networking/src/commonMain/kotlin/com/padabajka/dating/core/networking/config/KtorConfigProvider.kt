package com.padabajka.dating.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig

internal sealed interface KtorConfigProvider {

    interface Static : KtorConfigProvider {
        val config: HttpClientConfig<HttpClientEngineConfig>
    }
    interface Dynamic : KtorConfigProvider {
        suspend fun config(): HttpClientConfig<HttpClientEngineConfig>
    }
}
