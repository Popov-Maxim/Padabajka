package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import kotlinx.coroutines.flow.Flow

interface SearchPreferencesRepository {
    val searchPreferences: Flow<SearchPreferences>
    suspend fun update(update: (SearchPreferences) -> SearchPreferences)
}
