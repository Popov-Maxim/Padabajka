package com.padabajka.dating.core.networking.config

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.bodyAsText

class LoggingConfigProvider : KtorConfigProvider.Static {
    private val requestLoggingPlugin = createClientPlugin("CustomLoggingPlugin") {
        onRequest { request, content ->
            println("Request: ${request.method.value} ${request.url} | content $content")
        }
    }

    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(requestLoggingPlugin)

            install(ResponseObserver) {
                onResponse { response ->
                    val body = response.bodyAsText()
                    println("Response: ${response.status}, Body: $body")
                }
            }

            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { exception, request ->
                    println("Request exception: $exception | ${request.method.value} ${request.url}")
                }
            }
        }
}
