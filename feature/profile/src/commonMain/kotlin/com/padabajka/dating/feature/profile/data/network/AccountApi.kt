package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.throwIfNotSuccessful
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.http.path

class AccountApi(
    private val ktorClientProvider: KtorClientProvider
) {
    /**
     * DELETE /account
     */
    suspend fun delete() {
        val client = ktorClientProvider.client()

        val response = client.delete {
            url {
                path("/account")
            }
        }

        response.throwIfNotSuccessful()
    }

    suspend fun freeze() {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path("/account/freeze")
            }
        }

        response.throwIfNotSuccessful()
    }

    suspend fun unfreeze() {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path("/account/unfreeze")
            }
        }

        response.throwIfNotSuccessful()
    }
}
