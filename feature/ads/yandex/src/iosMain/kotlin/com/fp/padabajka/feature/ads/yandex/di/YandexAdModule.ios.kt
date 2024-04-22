package com.fp.padabajka.feature.ads.yandex.di

import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.feature.ads.yandex.YandexNativeAdLoader
import org.koin.core.module.Module
import org.koin.dsl.module

actual val yandexAdModule: Module = module {
    factory<NativeAdLoader> {
        YandexNativeAdLoader()
    }
}
