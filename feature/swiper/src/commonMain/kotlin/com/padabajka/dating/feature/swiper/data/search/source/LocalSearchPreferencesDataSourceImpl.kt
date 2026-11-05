package com.padabajka.dating.feature.swiper.data.search.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.core.repository.api.model.swiper.SearchPreferences
import com.padabajka.dating.feature.swiper.data.search.model.SearchPrefLocalCacheDto
import com.padabajka.dating.feature.swiper.data.search.model.toDto
import com.padabajka.dating.feature.swiper.data.search.model.toSearchPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalSearchPreferencesDataSourceImpl(
    private val dataStore: DataStore<SearchPrefLocalCacheDto>
) : LocalSearchPreferencesDataSource {

    override val searchPreferences: Flow<SearchPreferences?>
        get() = dataStore.data.map { it.preferences?.toSearchPref() }

    override suspend fun update(update: (SearchPreferences?) -> SearchPreferences?) {
        dataStore.updateData {
            SearchPrefLocalCacheDto(preferences = update(it.preferences?.toSearchPref())?.toDto())
        }
    }
}
