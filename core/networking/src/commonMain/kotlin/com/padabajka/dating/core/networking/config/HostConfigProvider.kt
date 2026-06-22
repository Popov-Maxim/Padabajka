package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.networking.NetworkConstants
import com.padabajka.dating.core.networking.protocol
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.model.settings.raw
import com.padabajka.dating.core.repository.api.model.settings.rawPort
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.DefaultRequest

class HostConfigProvider(
    private val appSettings: AppSettingsRepository
) : KtorConfigProvider.Static {

    override val config: HttpClientConfig<HttpClientEngineConfig>
        get() = httpClientConfig {
            install(DefaultRequest) {
                url {
                    val debugHost = appSettings.debugAppSettingsValue.host
                    protocol = debugHost.protocol() ?: NetworkConstants.protocol
                    host = debugHost.raw() ?: NetworkConstants.domainName
                    port = debugHost.rawPort() ?: NetworkConstants.PORT
                }
            }
        }
}
