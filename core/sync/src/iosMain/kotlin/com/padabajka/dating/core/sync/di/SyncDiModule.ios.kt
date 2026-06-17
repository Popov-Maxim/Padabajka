package com.padabajka.dating.core.sync.di

import com.padabajka.dating.core.sync.lifecycle.AppLifecycle
import com.padabajka.dating.core.sync.lifecycle.IosAppLifecycle
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformSyncDiModule: Module = module {
    singleOf(::IosAppLifecycle) { bind<AppLifecycle>() }
}
