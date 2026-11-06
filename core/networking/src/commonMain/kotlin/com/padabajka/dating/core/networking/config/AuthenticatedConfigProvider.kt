package com.padabajka.dating.core.networking.config

import com.padabajka.dating.core.repository.api.AuthRepository
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.HttpHeaders

internal class AuthenticatedConfigProvider(
    private val authRepository: AuthRepository
) : KtorConfigProvider.Dynamic {

    private val authenticatedRequestPlugin = createClientPlugin("AuthenticatedRequest") {
        onRequest { request, _ ->
            val authToken = authRepository.authToken()
                ?: TODO("Ktor: add auth exception") // TODO(P1)
            request.headers[HttpHeaders.Authorization] = "Bearer $authToken"
        }
    }

    override suspend fun config(): HttpClientConfig<HttpClientEngineConfig> {
        return httpClientConfig {
            install(authenticatedRequestPlugin)
        }
    }
}
