package com.padabajka.dating.settings.di

import com.padabajka.dating.settings.presentation.SettingScreenComponent
import org.koin.dsl.module

private val presentationModule = module {
    factory<SettingScreenComponent> { parameters ->
        SettingScreenComponent(
            context = parameters.get(),
            navigateBack = parameters.get(),
            logoutUseCaseFactory = { get() }
        )
    }
}

val settingDiModules = arrayOf(presentationModule)
