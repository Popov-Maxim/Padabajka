package com.padabajka.dating.settings.data

import androidx.datastore.core.DataStore
import com.padabajka.dating.core.repository.api.model.settings.DebugAppSettings
import kotlinx.coroutines.flow.Flow

class LocalDebugAppSettingsDataStore(
    private val dataStore: DataStore<DebugAppSettings>
) {
    val appSettings: Flow<DebugAppSettings> = dataStore.data

    suspend fun updateAppSettings(update: DebugAppSettings.() -> DebugAppSettings) {
        dataStore.updateData(update)
    }
}
