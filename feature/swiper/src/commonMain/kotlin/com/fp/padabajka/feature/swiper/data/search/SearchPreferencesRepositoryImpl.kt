package com.fp.padabajka.feature.swiper.data.search

import com.fp.padabajka.core.repository.api.SearchPreferencesRepository
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.search.source.LocalSearchPreferencesDataSource
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
