package com.padabajka.dating.feature.swiper.data.search.source

import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import kotlinx.coroutines.flow.Flow

interface LocalSearchPreferencesDataSource {
    val searchPreferences: Flow<SearchPreferences>
    suspend fun update(update: (SearchPreferences) -> SearchPreferences)
}
