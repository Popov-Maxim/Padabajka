package com.padabajka.dating.di

import com.padabajka.dating.deeplink.DeeplinkParser
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val sharedDiModule = module {
    factoryOf(::DeeplinkParser)
}
