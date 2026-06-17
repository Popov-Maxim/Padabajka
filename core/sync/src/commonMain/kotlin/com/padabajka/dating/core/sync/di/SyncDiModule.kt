package com.padabajka.dating.core.sync.di

import com.padabajka.dating.core.sync.SyncManager
import com.padabajka.dating.core.sync.SyncSessionObserver
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val platformSyncDiModule: Module

private val common = module {
    singleOf(::SyncManager)
    singleOf(::SyncSessionObserver)
}

val syncDiModule = arrayOf(common, platformSyncDiModule)
