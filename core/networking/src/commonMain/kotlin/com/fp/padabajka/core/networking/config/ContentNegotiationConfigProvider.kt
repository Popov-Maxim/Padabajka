package com.fp.padabajka.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class ContentNegotiationConfigProvider : KtorConfigProvider.Static {

    override val config: HttpClientConfig<Nothing>
        get() = HttpClientConfig<Nothing>().apply {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
}
