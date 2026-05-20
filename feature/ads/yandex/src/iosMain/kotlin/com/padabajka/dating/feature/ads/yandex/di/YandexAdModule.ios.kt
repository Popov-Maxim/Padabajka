package com.padabajka.dating.feature.ads.yandex.di

import com.padabajka.dating.core.data.NativeAdLoader
import com.padabajka.dating.feature.ads.yandex.YandexNativeAdLoader
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalForeignApi::class)
actual val yandexAdModule: Module = module {
    factory<NativeAdLoader> {
        YandexNativeAdLoader()
    }
}
