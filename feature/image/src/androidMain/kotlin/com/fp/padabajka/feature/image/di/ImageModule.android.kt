package com.fp.padabajka.feature.image.di

import com.fp.padabajka.feature.image.data.source.AndroidLocalImageDataSource
import com.fp.padabajka.feature.image.data.source.LocalImageDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    factory<LocalImageDataSource> {
        AndroidLocalImageDataSource(
            context = get()
        )
    }
}
