package com.padabajka.dating.core.domain.di

import com.padabajka.dating.core.domain.sync.SyncRemoteDataUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal expect val platformDomainDiModule: Module

private val domainDiModule = module {
    factoryOf(::SyncRemoteDataUseCase)
}

val domainDiModules: Array<Module>
    get() = arrayOf(platformDomainDiModule, domainDiModule)
