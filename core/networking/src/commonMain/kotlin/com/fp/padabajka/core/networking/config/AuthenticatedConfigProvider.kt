package com.fp.padabajka.core.networking.config

import com.fp.padabajka.core.repository.api.AuthRepository
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

internal class AuthenticatedConfigProvider(
    private val authRepository: AuthRepository
) : KtorConfigProvider.Dynamic {

    override suspend fun config(): HttpClientConfig<HttpClientEngineConfig> {
        return httpClientConfig {
            install(Auth) {
                bearer {
                    loadTokens {
                        val authToken = authRepository.authToken() ?: TODO("Ktor: add auth exception")
                        BearerTokens(authToken, "")
                    }
                }
            }
        }
    }
}
