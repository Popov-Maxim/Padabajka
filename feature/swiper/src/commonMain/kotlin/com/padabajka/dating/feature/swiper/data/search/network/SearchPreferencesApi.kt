package com.padabajka.dating.feature.swiper.data.search.network

import com.padabajka.dating.feature.swiper.data.search.network.model.SearchPreferencesResponse

interface SearchPreferencesApi {
    /**
     * GET /search-preferences
     */
    suspend fun get(): SearchPreferencesResponse?

    companion object {
        const val PATH = "search-preferences"
    }
}
