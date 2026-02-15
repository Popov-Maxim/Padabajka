package com.padabajka.dating.core.system.intent.di

import com.padabajka.dating.core.system.intent.SystemNavigator
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal expect val platformSystemIntentDiModule: Module
private val commonSystemIntentDiModule = module {
    singleOf(::SystemNavigator)
}

val systemIntentDiModule = arrayOf(platformSystemIntentDiModule, commonSystemIntentDiModule)
