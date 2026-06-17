package com.padabajka.dating.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.websocket.WebSockets
import kotlin.time.Duration.Companion.seconds

class WebSocketConfigProvider : KtorConfigProvider.Static {
    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(WebSockets) {
                pingInterval = 30.seconds.inWholeMilliseconds
            }
        }
}
