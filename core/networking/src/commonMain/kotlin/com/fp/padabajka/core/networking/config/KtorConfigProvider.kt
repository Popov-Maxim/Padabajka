package com.fp.padabajka.core.networking.config

import io.ktor.client.HttpClientConfig

internal interface KtorConfigProvider {
    suspend fun needUpdate(): Boolean
    suspend fun config(): HttpClientConfig<Nothing>
}
