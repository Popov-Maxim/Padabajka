package com.padabajka.dating.feature.profile.data.network

import com.padabajka.dating.core.data.network.model.PersonResponse
import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.appendNotNull
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.path

// TODO(network): need to separate?
class KtorProfileApi(
    private val ktorClientProvider: KtorClientProvider
) : ProfileApi {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun get(): PersonResponse? {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(ProfileApi.PATH_FOR_GET)
            }
        }

        return response.takeIf { it.status != HttpStatusCode.NoContent }
            ?.body()
    }

    override suspend fun get(userId: String): PersonResponse? {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(ProfileApi.PATH_FOR_GET + "/$userId")
            }
        }

        return response.takeIf { it.status != HttpStatusCode.NoContent }
            ?.body()
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

    override suspend fun create(profile: ProfileDto) {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(ProfileApi.PATH_FOR_CREATE_API)
            }

            contentType(ContentType.Application.Json)
            setBody(profile)
        }

        if (response.status != HttpStatusCode.OK) TODO()
    }
}
