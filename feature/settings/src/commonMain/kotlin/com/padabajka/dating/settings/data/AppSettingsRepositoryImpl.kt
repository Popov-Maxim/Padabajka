package com.padabajka.dating.settings.data

import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.model.settings.AppSettings
import com.padabajka.dating.core.repository.api.model.settings.DebugAppSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AppSettingsRepositoryImpl(
    coroutineScope: CoroutineScope,
    private val localAppSettingsDataStore: LocalAppSettingsDataStore,
    private val localDebugAppSettingsDataStore: LocalDebugAppSettingsDataStore,
) : AppSettingsRepository {

    private val _debugAppSettings = localDebugAppSettingsDataStore.appSettings
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = DebugAppSettings()
        )

    override val appSettings: Flow<AppSettings>
        get() = localAppSettingsDataStore.appSettings

    override val debugAppSettings: StateFlow<DebugAppSettings>
        get() = _debugAppSettings
    override val debugAppSettingsValue: DebugAppSettings
        get() = _debugAppSettings.value

    override suspend fun updateAppSettings(update: AppSettings.() -> AppSettings) {
        localAppSettingsDataStore.updateAppSettings(update)
    }

    override suspend fun updateDebugAppSettings(update: DebugAppSettings.() -> DebugAppSettings) {
        localDebugAppSettingsDataStore.updateAppSettings(update)
    }
}
