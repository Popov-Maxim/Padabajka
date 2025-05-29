package com.padabajka.dating.settings.di

import com.padabajka.dating.settings.domain.SyncRemoteDataUseCase
import com.padabajka.dating.settings.presentation.SettingScreenComponent
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

private val presentationModule = module {
    factory<SettingScreenComponent> { parameters ->
        SettingScreenComponent(
            context = parameters.get(),
            navigateBack = parameters.get(),
            logoutUseCaseFactory = { get() },
            saveTokenUseCase = get(),
            syncRemoteDataUseCase = get()
        )
    }

    factoryOf(::SyncRemoteDataUseCase)
}

val settingDiModules = arrayOf(presentationModule)
