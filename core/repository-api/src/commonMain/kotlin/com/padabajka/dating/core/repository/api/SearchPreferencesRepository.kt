package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import kotlinx.coroutines.flow.Flow

interface SearchPreferencesRepository {
    val searchPreferences: Flow<SearchPreferences>
    suspend fun update(update: (SearchPreferences) -> SearchPreferences)
}
