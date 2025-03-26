package com.padabajka.dating.feature.swiper.data.search

import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.data.search.source.LocalSearchPreferencesDataSource
import kotlinx.coroutines.flow.Flow

class SearchPreferencesRepositoryImpl(
    private val localDataSource: LocalSearchPreferencesDataSource
) : SearchPreferencesRepository {

    override val searchPreferences: Flow<SearchPreferences>
        get() = localDataSource.searchPreferences

    override suspend fun update(update: (SearchPreferences) -> SearchPreferences) {
        localDataSource.update(update)
    }
}
