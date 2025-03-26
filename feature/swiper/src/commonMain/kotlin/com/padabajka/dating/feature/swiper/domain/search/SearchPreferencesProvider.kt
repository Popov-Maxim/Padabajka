package com.padabajka.dating.feature.swiper.domain.search

import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import kotlinx.coroutines.flow.Flow

class SearchPreferencesProvider(
    private val searchPreferencesRepository: SearchPreferencesRepository
) {
    fun get(): Flow<SearchPreferences> = searchPreferencesRepository.searchPreferences
}
