package com.padabajka.dating.feature.ads.yandex.di

import com.padabajka.dating.core.data.NativeAdLoader
import com.padabajka.dating.feature.ads.yandex.YandexNativeAdLoader
import org.koin.core.module.Module
import org.koin.dsl.module

actual val yandexAdModule: Module = module {
    factory<NativeAdLoader> {
        YandexNativeAdLoader()
    }
}
