package com.fp.padabajka.core.presentation.di

import coil3.ImageLoader
import coil3.util.DebugLogger
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentationDiModule = module {
    single<ImageLoader> { parameters ->
        ImageLoader.Builder(parameters.get())
            .logger(DebugLogger())
            .components {
//                add(
//                    KtorNetworkFetcherFactory(
//                        httpClient = HttpClient(engine = imageEngine)
//                    )
//                )
            }
            .build()
    }
}

val presentationDiModules: Array<Module>
    get() = arrayOf(presentationDiModule)
