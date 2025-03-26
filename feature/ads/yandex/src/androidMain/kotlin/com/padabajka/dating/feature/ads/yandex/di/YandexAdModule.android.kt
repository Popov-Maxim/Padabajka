package com.padabajka.dating.feature.ads.yandex.di

import com.padabajka.dating.core.data.NativeAdLoader
import com.padabajka.dating.feature.ads.yandex.YandexNativeAdLoader
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
