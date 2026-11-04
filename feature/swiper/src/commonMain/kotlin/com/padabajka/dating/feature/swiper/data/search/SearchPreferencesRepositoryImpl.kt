package com.padabajka.dating.feature.swiper.data.search

import com.padabajka.dating.core.repository.api.SearchPreferencesRepository
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.core.utils.runCatchingCancellable
import com.padabajka.dating.feature.swiper.data.search.network.SearchPreferencesApi
import com.padabajka.dating.feature.swiper.data.search.network.model.toDomain
import com.padabajka.dating.feature.swiper.data.search.source.LocalSearchPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SearchPreferencesRepositoryImpl(
    private val localDataSource: LocalSearchPreferencesDataSource,
    private val api: SearchPreferencesApi,
) : SearchPreferencesRepository {

    override val searchPreferences: Flow<SearchPreferences?>
        get() = flow {
            val cached = localDataSource.searchPreferences.first()
            if (cached == null) {
                val loaded = runCatchingCancellable { api.get()?.toDomain() }.getOrNull()
                if (loaded != null) {
                    localDataSource.update { it ?: loaded }
                }
            }
            emitAll(localDataSource.searchPreferences)
        }

    override suspend fun update(update: (SearchPreferences?) -> SearchPreferences) {
        localDataSource.update(update)
    }

    override suspend fun clearLocalData() {
        localDataSource.update { null }
    }
}
