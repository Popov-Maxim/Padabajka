package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.settings.AppSettings
import com.padabajka.dating.core.repository.api.model.settings.DebugAppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AppSettingsRepository {
    val appSettings: Flow<AppSettings>
    val debugAppSettings: StateFlow<DebugAppSettings>
    val debugAppSettingsValue: DebugAppSettings

    suspend fun updateAppSettings(update: AppSettings.() -> AppSettings)
    suspend fun updateDebugAppSettings(update: DebugAppSettings.() -> DebugAppSettings)
}
