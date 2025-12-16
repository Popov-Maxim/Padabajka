package com.padabajka.dating.core.sync.di

import com.padabajka.dating.core.sync.SyncManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val syncDiModule = module {
    singleOf(::SyncManager)
}
