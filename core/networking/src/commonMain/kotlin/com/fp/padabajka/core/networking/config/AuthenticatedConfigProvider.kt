package com.fp.padabajka.core.networking.config

import com.fp.padabajka.core.repository.api.AuthRepository
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

internal class AuthenticatedConfigProvider(
    private val authRepository: AuthRepository
) : KtorConfigProvider {

    private var currentAuthToken: String? = null

    override suspend fun needUpdate(): Boolean {
        return authRepository.authToken() != currentAuthToken
    }

    override suspend fun config(): HttpClientConfig<Nothing> {
        return HttpClientConfig<Nothing>().apply {
            install(Auth) {
                bearer {
                    loadTokens {
                        val newAuthToken = authRepository.authToken()
                        currentAuthToken = newAuthToken
                        BearerTokens(newAuthToken ?: "", "")
                    }
                }
            }
        }
    }
}
