package com.fp.padabajka.feature.profile.di

import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.feature.profile.data.ProfileRepositoryImpl
import org.koin.dsl.module

private val dataModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl()
    }
}

val profileModules = arrayOf(dataModule)
