package com.fp.padabajka.feature.ads.yandex.di

import cocoapods.YandexMobileAds.YMANativeAdLoader
import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.feature.ads.yandex.YandexNativeAdLoader
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module

@OptIn(ExperimentalForeignApi::class)
actual val yandexAdModule = module {
    factory<NativeAdLoader> {
        YandexNativeAdLoader(
            nativeAdLoader = get()
        )
    }

    factory<YMANativeAdLoader> {
        YMANativeAdLoader()
    }
}
