package com.padabajka.dating.core.sync.di

import com.padabajka.dating.core.sync.lifecycle.AndroidAppLifecycle
import com.padabajka.dating.core.sync.lifecycle.AppLifecycle
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformSyncDiModule: Module = module {
    singleOf(::AndroidAppLifecycle) { bind<AppLifecycle>() }
}
