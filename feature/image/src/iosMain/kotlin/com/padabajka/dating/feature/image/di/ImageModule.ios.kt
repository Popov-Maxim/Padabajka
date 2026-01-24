package com.padabajka.dating.feature.image.di

import com.padabajka.dating.feature.image.data.platform.IosImageCompressor
import com.padabajka.dating.feature.image.data.source.IosLocalImageDataSource
import com.padabajka.dating.feature.image.data.source.LocalImageDataSource
import com.padabajka.dating.feature.image.domain.ImageCompressor
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factory<LocalImageDataSource> {
        IosLocalImageDataSource()
    }

    single<ImageCompressor> {
        IosImageCompressor()
    }
}
