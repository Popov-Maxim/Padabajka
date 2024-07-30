package com.fp.padabajka.feature.swiper.data.search.source

import androidx.datastore.core.DataStore
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import kotlinx.coroutines.flow.Flow

class LocalSearchPreferencesDataSourceImpl(
    private val dataStore: DataStore<SearchPreferences>
) : LocalSearchPreferencesDataSource {

    override val searchPreferences: Flow<SearchPreferences>
        get() = dataStore.data

    override suspend fun update(update: (SearchPreferences) -> SearchPreferences) {
        dataStore.updateData(update)
    }
}
