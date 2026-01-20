package com.padabajka.dating.feature.profile.data.asset.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.feature.profile.data.asset.model.InterestAssetDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

class InterestAssetApi(
    private val ktorClientProvider: KtorClientProvider
) {
    suspend fun getAssets(): List<InterestAssetDto> {
        val client = ktorClientProvider.client()

        val response = client.get {
            url {
                path(PATH_FOR_GET)
            }
        }

        return response.body()
    }

    companion object {
        private const val PATH_FOR_GET = "assets/interests"
    }
}
