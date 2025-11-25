package com.padabajka.dating.feature.auth.data.remote

import com.padabajka.dating.core.networking.UnauthKtorClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.http.path
import org.koin.core.component.KoinComponent

class DebugAuthApi(
    private val ktorClientProvider: UnauthKtorClientProvider
) : KoinComponent {

    suspend fun login(uuid: String): String {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path("$DEBUG_LOGIN_PATH/$uuid")
            }
        }

        return response.body()
    }

    private companion object {
        private const val DEBUG_LOGIN_PATH = "debug_login"
    }
}
