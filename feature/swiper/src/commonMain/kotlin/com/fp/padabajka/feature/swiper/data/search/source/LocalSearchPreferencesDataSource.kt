package com.fp.padabajka.feature.swiper.data.search.source

import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import kotlinx.coroutines.flow.Flow

interface LocalSearchPreferencesDataSource {
    val searchPreferences: Flow<SearchPreferences>
    suspend fun update(update: (SearchPreferences) -> SearchPreferences)
}
