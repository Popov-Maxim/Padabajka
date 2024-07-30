package com.fp.padabajka.feature.swiper.domain.search

import com.fp.padabajka.core.repository.api.SearchPreferencesRepository
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import kotlinx.coroutines.flow.Flow

class SearchPreferencesProvider(
    private val searchPreferencesRepository: SearchPreferencesRepository
) {
    fun get(): Flow<SearchPreferences> = searchPreferencesRepository.searchPreferences
}
