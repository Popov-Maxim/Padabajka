package com.padabajka.dating.feature.image.di

import com.padabajka.dating.core.repository.api.ImageRepository
import com.padabajka.dating.feature.image.data.ImageRepositoryImpl
import com.padabajka.dating.feature.image.data.network.ImageApi
import com.padabajka.dating.feature.image.data.network.KtorImageApi
import com.padabajka.dating.feature.image.data.source.RemoveImageDataSource
import com.padabajka.dating.feature.image.data.source.RemoveImageDataSourceImpl
import com.padabajka.dating.feature.image.domain.GetLocalImageUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal expect val platformModule: Module

private val dataModule = module {
    single<ImageRepository> {
        ImageRepositoryImpl(
            removeImageDataSource = get(),
            localImageDataSource = get()
        )
    }

    factory<RemoveImageDataSource> {
        RemoveImageDataSourceImpl(
            imageApi = get()
        )
    }

    factory<ImageApi> {
        KtorImageApi(
            ktorClientProvider = get()
        )
    }
}

private val domain = module {
    factoryOf(::GetLocalImageUseCase)
}

val imageModules: Array<Module>
    get() = arrayOf(dataModule, domain, platformModule)
