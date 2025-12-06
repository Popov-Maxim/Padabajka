package com.padabajka.dating.settings.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.model.settings.AppSettings
import com.padabajka.dating.core.repository.api.model.settings.DebugAppSettings
import com.padabajka.dating.settings.data.AppSettingsRepositoryImpl
import com.padabajka.dating.settings.data.LocalAppSettingsDataStore
import com.padabajka.dating.settings.data.LocalDebugAppSettingsDataStore
import com.padabajka.dating.settings.presentation.SettingScreenComponent
import org.koin.dsl.module

private val dataModule = module {

    single<AppSettingsRepository> {
        AppSettingsRepositoryImpl(
            coroutineScope = get(),
            localAppSettingsDataStore = get(),
            localDebugAppSettingsDataStore = get()
        )
    }

    factory<LocalAppSettingsDataStore> { parameters ->
        LocalAppSettingsDataStore(
            dataStore = DataStoreUtils.create(
                "app_settings_storage",
                AppSettings.serializer(),
                AppSettings()
            )
        )
    }

    factory<LocalDebugAppSettingsDataStore> { parameters ->
        LocalDebugAppSettingsDataStore(
            dataStore = DataStoreUtils.create(
                "app_debug_settings_storage",
                DebugAppSettings.serializer(),
                DebugAppSettings()
            )
        )
    }
}

private val presentationModule = module {
    factory<SettingScreenComponent> { parameters ->
        SettingScreenComponent(
            context = parameters.get(),
            navigateBack = parameters.get(),
            openPermissionFlow = parameters.get(),
            logoutUseCaseFactory = { get() },
            saveTokenUseCase = get(),
            syncRemoteDataUseCase = get()
        )
    }
}

val settingDiModules = arrayOf(dataModule, presentationModule)
