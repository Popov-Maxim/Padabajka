package com.fp.padabajka.di

import com.fp.padabajka.feature.auth.di.authModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(platformModule: Module) {
    startKoin { modules(*authModules, platformModule) }
}
