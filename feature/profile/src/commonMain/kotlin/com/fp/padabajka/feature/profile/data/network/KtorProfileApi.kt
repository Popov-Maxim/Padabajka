package com.fp.padabajka.feature.profile.data.network

import com.fp.padabajka.core.data.network.model.PersonResponse
import com.fp.padabajka.core.networking.KtorClientProvider
import com.fp.padabajka.core.networking.utils.appendNotNull
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.http.HttpStatusCode
import io.ktor.http.path

// TODO(network): need to separate?
class KtorProfileApi(
    private val ktorClientProvider: KtorClientProvider
) : ProfileApi {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun get(): PersonResponse {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(ProfileApi.PATH_FOR_GET)
            }
        }

        return response.body()
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun patch(params: ProfileApi.PatchParams) {
        val client = ktorClientProvider.client()

        val response = client.patch {
            url {
                path(ProfileApi.PATH_FOR_PATCH)

                parameters.apply {
                    params.raw.forEach { (key, value) ->
                        appendNotNull(key.raw, value)
                    }
                }
            }
        }

        if (response.status != HttpStatusCode.OK) TODO()
    }
}
