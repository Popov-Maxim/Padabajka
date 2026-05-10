package com.padabajka.dating.di

import com.padabajka.dating.core.repository.api.DeeplinkHandler
import com.padabajka.dating.deeplink.DeeplinkHandlerImpl
import com.padabajka.dating.deeplink.DeeplinkParser
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedDiModule = module {
    factoryOf(::DeeplinkParser)
    singleOf(::DeeplinkHandlerImpl) {
        bind<DeeplinkHandler>()
    }
}
