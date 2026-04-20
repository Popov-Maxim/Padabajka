package com.padabajka.dating.feature.profile.data.asset.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.utils.takeIfHasContent
import com.padabajka.dating.feature.profile.data.asset.model.AssetsDto
import com.padabajka.dating.feature.profile.data.asset.model.CityDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

class CityApi(
    private val ktorClientProvider: KtorClientProvider
) {
    suspend fun getCities(clientVersion: Int): AssetsDto<CityDto>? {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path("$PATH_FOR_GET/$clientVersion")
            }
        }

        return response
            .takeIfHasContent()
            ?.body()
    }

    companion object {
        private const val PATH_FOR_GET = "assets/cities"
    }
}
