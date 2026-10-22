package com.padabajka.dating.core.platform.data.di

import com.padabajka.dating.core.platform.data.activity.AndroidActivityProvider
import com.padabajka.dating.core.platform.data.geo.AndroidPlatformLocationProvider
import com.padabajka.dating.core.platform.data.geo.PlatformLocationProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformDiModule = module {
    single(createdAtStart = true) { AndroidActivityProvider(get()) }

    singleOf(::AndroidPlatformLocationProvider) {
        bind<PlatformLocationProvider>()
    }
}
