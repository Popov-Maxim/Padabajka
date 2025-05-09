package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.domain.AppSettings
import com.padabajka.dating.core.networking.NetworkConstants
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.URLProtocol

class HostConfigProvider(
    private val appSettings: AppSettings
) : KtorConfigProvider.Static {

    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTP
                    host = appSettings.host ?: NetworkConstants.domainName
                    port = NetworkConstants.PORT
                }
            }
        }
}
