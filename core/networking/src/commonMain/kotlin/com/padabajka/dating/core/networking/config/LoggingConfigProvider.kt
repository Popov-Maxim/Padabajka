package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.utils.printlnDev
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.URLProtocol

class LoggingConfigProvider : KtorConfigProvider.Static {
    private val requestLoggingPlugin = createClientPlugin("CustomLoggingPlugin") {
        onRequest { request, content ->
            printlnDev("Request: ${request.method.value} ${request.url} | content $content")
        }
    }

    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(FirebasePlugin)
            install(requestLoggingPlugin)

            install(ResponseObserver) {
                onResponse { response ->
                    val request = response.request
                    val isWebSocket = request.url.protocol in arrayOf(URLProtocol.WS, URLProtocol.WSS)
                    if (isWebSocket) {
                        printlnDev("Response: ${response.status}, Socket request: ${request.url}")
                    } else {
                        val body = response.bodyAsText()
                        printlnDev("Response: ${response.status}, Body: $body | ${request.method} ${request.url}")
                    }
                }
            }

            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { exception, request ->
                    printlnDev("Request exception: $exception | ${request.method.value} ${request.url}")
                }
            }
        }
}
