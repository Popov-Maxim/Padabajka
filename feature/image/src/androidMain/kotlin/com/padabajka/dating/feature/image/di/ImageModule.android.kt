package com.padabajka.dating.feature.image.di

import com.padabajka.dating.feature.image.data.platform.AndroidImageCompressor
import com.padabajka.dating.feature.image.data.source.AndroidLocalImageDataSource
import com.padabajka.dating.feature.image.data.source.LocalImageDataSource
import com.padabajka.dating.feature.image.domain.ImageCompressor
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factory<LocalImageDataSource> {
        AndroidLocalImageDataSource(
            context = get()
        )
    }
    single<ImageCompressor> {
        AndroidImageCompressor()
    }
}
