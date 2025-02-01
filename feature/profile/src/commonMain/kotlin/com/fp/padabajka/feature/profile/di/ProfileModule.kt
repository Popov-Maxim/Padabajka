package com.fp.padabajka.feature.profile.di

import com.fp.padabajka.core.repository.api.ProfileRepository
import com.fp.padabajka.feature.profile.data.ProfileRepositoryImpl
import com.fp.padabajka.feature.profile.data.network.KtorProfileApi
import com.fp.padabajka.feature.profile.data.network.ProfileApi
import com.fp.padabajka.feature.profile.data.source.RemoveProfileDataSource
import com.fp.padabajka.feature.profile.data.source.RemoveProfileDataSourceImpl
import com.fp.padabajka.feature.profile.presentation.ProfileScreenComponent
import org.koin.dsl.module

private val dataModule = module {
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            removeProfileDataSource = get()
        )
    }

    factory<RemoveProfileDataSource> {
        RemoveProfileDataSourceImpl(
            profileApi = get()
        )
    }

    factory<ProfileApi> {
        KtorProfileApi(
            ktorClientProvider = get(),
            appSettings = get()
        )
    }
}

private val presentationModule = module {
    factory<ProfileScreenComponent> { parameters ->
        ProfileScreenComponent(
            context = parameters.get(),
            openEditor = parameters.get(),
            profileRepository = get()
        )
    }
}

val profileModules = arrayOf(dataModule, presentationModule)
