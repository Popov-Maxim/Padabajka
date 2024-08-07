package com.fp.padabajka.feature.swiper.data.search.source

import androidx.datastore.core.DataStore
import com.fp.padabajka.core.repository.api.model.swiper.SearchPreferences
import com.fp.padabajka.feature.swiper.data.search.model.SearchPrefDto
import com.fp.padabajka.feature.swiper.data.search.model.toDto
import com.fp.padabajka.feature.swiper.data.search.model.toSearchPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalSearchPreferencesDataSourceImpl(
    private val dataStore: DataStore<SearchPrefDto>
) : LocalSearchPreferencesDataSource {

    override val searchPreferences: Flow<SearchPreferences>
        get() = dataStore.data.map { it.toSearchPref() }

    override suspend fun update(update: (SearchPreferences) -> SearchPreferences) {
        dataStore.updateData { update(it.toSearchPref()).toDto() }
    }
}
