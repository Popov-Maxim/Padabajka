package com.padabajka.dating.core.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val platformDomainDiModule: Module

private val domainDiModule = module {
}

val domainDiModules: Array<Module>
    get() = arrayOf(platformDomainDiModule, domainDiModule)
