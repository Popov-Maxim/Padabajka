package com.padabajka.dating.settings.di

import com.padabajka.dating.core.data.utils.DataStoreUtils
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.model.settings.AppSettings
import com.padabajka.dating.core.repository.api.model.settings.DebugAppSettings
import com.padabajka.dating.settings.data.AppSettingsRepositoryImpl
import com.padabajka.dating.settings.data.LocalAppSettingsDataStore
import com.padabajka.dating.settings.data.LocalDebugAppSettingsDataStore
import com.padabajka.dating.settings.domain.AppSettingsComponentProvider
import com.padabajka.dating.settings.domain.ChangeLanguageUseCase
import com.padabajka.dating.settings.presentation.SettingScreenComponent
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
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

private val domain = module {
    factoryOf(::ChangeLanguageUseCase)
    singleOf(::AppSettingsComponentProvider)
}

private val presentationModule = module {
    factory<SettingScreenComponent> { parameters ->
        SettingScreenComponent(
            context = parameters.get(),
            navigateBack = parameters.get(),
            settingNavigator = parameters.get(),
            logoutUseCaseFactory = { get() },
            saveTokenUseCase = get(),
            syncRemoteDataUseCase = get(),
            settingsComponentProvider = get(),
        )
    }
}

val settingDiModules = arrayOf(dataModule, domain, presentationModule)
