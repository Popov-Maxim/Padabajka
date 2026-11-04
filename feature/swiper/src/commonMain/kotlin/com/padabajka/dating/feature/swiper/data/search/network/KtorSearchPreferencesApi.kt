package com.padabajka.dating.feature.swiper.data.search.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.feature.swiper.data.search.network.model.SearchPreferencesResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.path

class KtorSearchPreferencesApi(
    private val ktorClientProvider: KtorClientProvider,
) : SearchPreferencesApi {
    override suspend fun get(): SearchPreferencesResponse? {
        val client = ktorClientProvider.client()
        val response = client.get {
            url { path(SearchPreferencesApi.PATH) }
        }

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }
}
