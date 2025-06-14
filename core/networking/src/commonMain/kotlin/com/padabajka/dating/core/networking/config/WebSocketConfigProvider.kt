package com.padabajka.dating.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.websocket.WebSockets

class WebSocketConfigProvider : KtorConfigProvider.Static {
    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(WebSockets)
        }
}
