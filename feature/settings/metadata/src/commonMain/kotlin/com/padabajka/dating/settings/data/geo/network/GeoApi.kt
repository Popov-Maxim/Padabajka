package com.padabajka.dating.settings.data.geo.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.throwIfNotSuccessful
import com.padabajka.dating.settings.data.geo.module.LocationRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

class GeoApi(
    private val ktorClientProvider: KtorClientProvider
) {

    /**
     * POST /location
     */
    suspend fun send(locationRequest: LocationRequest) {
        val client = ktorClientProvider.client()

        val response = client.post {
            url {
                path(PATH)
            }

            contentType(ContentType.Application.Json)
            setBody(locationRequest)
        }

        return response.throwIfNotSuccessful()
    }

    companion object {
        private const val PATH = "location"
    }
}
