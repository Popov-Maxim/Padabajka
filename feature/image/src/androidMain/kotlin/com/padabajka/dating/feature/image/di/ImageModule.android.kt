package com.padabajka.dating.feature.image.di

import com.padabajka.dating.feature.image.data.source.AndroidLocalImageDataSource
import com.padabajka.dating.feature.image.data.source.LocalImageDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factory<LocalImageDataSource> {
        AndroidLocalImageDataSource(
            context = get()
        )
    }
}
