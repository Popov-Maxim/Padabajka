package com.padabajka.dating.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig

fun httpClientConfig(
    block: HttpClientConfig<HttpClientEngineConfig>.() -> Unit
): HttpClientConfig<HttpClientEngineConfig> = HttpClientConfig<HttpClientEngineConfig>().apply(block)
