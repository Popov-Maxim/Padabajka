package com.fp.padabajka.core.networking.config

import io.ktor.client.HttpClientConfig

internal sealed interface KtorConfigProvider {

    interface Static : KtorConfigProvider {
        val config: HttpClientConfig<Nothing>
    }
    interface Dynamic : KtorConfigProvider {
        suspend fun config(): HttpClientConfig<Nothing>
    }
}
