package com.padabajka.dating.core.presentation.di

import coil3.ImageLoader
import coil3.network.ktor2.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import com.padabajka.dating.core.networking.imageEngine
import com.padabajka.dating.core.presentation.ComponentLifecycleListener
import com.padabajka.dating.core.presentation.error.DomainErrorHandler
import com.padabajka.dating.core.presentation.event.AlertService
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val presentationDiModule = module {
    single<ImageLoader> { parameters ->
        ImageLoader.Builder(parameters.get())
            .logger(DebugLogger())
            .components {
                add(
                    KtorNetworkFetcherFactory(
                        httpClient = HttpClient(engine = imageEngine)
                    )
                )
                add(SvgDecoder.Factory(scaleToDensity = true))
            }
            .build()
    }

    factory<ComponentLifecycleListener> { parameters ->
        ComponentLifecycleListener(
            screenName = parameters.get(),
            frameObserver = get(),
            frameStorage = get(),
            coroutineScope = get(),
            frameMetricsAggregator = get()
        )
    }

    singleOf(::AlertService)
    factoryOf(::DomainErrorHandler)
}

val presentationDiModules: Array<Module>
    get() = arrayOf(presentationDiModule)
