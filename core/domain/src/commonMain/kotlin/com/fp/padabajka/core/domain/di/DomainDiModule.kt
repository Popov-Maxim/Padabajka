package com.fp.padabajka.core.domain.di

import com.fp.padabajka.core.domain.AppSettings
import com.fp.padabajka.core.domain.MutableAppSettings
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformDomainDiModule: Module

private val domainDiModule = module {
    single<MutableAppSettings> {
        MutableAppSettings()
    }

    single<AppSettings> {
        get<MutableAppSettings>()
    }
}

val domainDiModules: Array<Module>
    get() = arrayOf(platformDomainDiModule, domainDiModule)
