package com.fp.padabajka.feature.ads.yandex.di

import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.feature.ads.yandex.YandexNativeAdLoader
import org.koin.dsl.module
import com.yandex.mobile.ads.nativeads.NativeAdLoader as SdkNativeAdLoader

actual val yandexAdModule = module {
    factory<NativeAdLoader> {
        YandexNativeAdLoader(
            nativeAdLoader = get()
        )
    }

    factory<SdkNativeAdLoader> {
        SdkNativeAdLoader(get())
    }
}
