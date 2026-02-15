package com.padabajka.dating.core.platform.data.di

import com.padabajka.dating.core.platform.data.geo.IOSLocationProvider
import com.padabajka.dating.core.platform.data.geo.PlatformLocationProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformDiModule = module {
    singleOf(::IOSLocationProvider) {
        bind<PlatformLocationProvider>()
    }
}
