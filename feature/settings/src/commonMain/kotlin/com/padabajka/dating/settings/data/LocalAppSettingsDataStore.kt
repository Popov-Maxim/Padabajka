package com.padabajka.dating.settings.data

import androidx.datastore.core.DataStore
import com.padabajka.dating.core.repository.api.model.settings.AppSettings
import kotlinx.coroutines.flow.Flow

class LocalAppSettingsDataStore(
    private val dataStore: DataStore<AppSettings>
) {
    val appSettings: Flow<AppSettings> = dataStore.data

    suspend fun updateAppSettings(update: AppSettings.() -> AppSettings) {
        dataStore.updateData(update)
    }
}
