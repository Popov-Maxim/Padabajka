package com.padabajka.dating.di

import com.padabajka.dating.domain.SyncRemoteDataUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val sharedDiModule = module {
    factoryOf(::SyncRemoteDataUseCase)
}
