package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.networking.NetworkConstants
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.model.settings.raw
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.URLProtocol

class HostConfigProvider(
    private val appSettings: AppSettingsRepository
) : KtorConfigProvider.Static {

    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTP
                    host = appSettings.debugAppSettingsValue.host.raw() ?: NetworkConstants.domainName
                    port = NetworkConstants.PORT
                }
            }
        }
}
