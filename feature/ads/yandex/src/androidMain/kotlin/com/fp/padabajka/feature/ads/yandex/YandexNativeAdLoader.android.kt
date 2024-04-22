package com.fp.padabajka.feature.ads.yandex

import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.feature.ads.yandex.model.PlatformNativeAdImpl
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import com.yandex.mobile.ads.nativeads.NativeAdLoader as SdkNativeAdLoader

actual class YandexNativeAdLoader(private val nativeAdLoader: SdkNativeAdLoader) : NativeAdLoader {

    private class ProxyListener(
        private val listener: NativeAdLoader.Listener
    ) : NativeAdLoadListener {
        override fun onAdLoaded(nativeAd: NativeAd) {
            listener.onLoaded(PlatformNativeAdImpl(nativeAd))
        }

        override fun onAdFailedToLoad(error: AdRequestError) {
            listener.onError(error.description)
        }
    }

    override fun setListener(listener: NativeAdLoader.Listener?) {
        nativeAdLoader.setNativeAdLoadListener(listener?.let { ProxyListener(it) })
    }

    override fun loadAd(configuration: NativeAdLoader.Configuration) {
        val nativeAdRequestConfiguration = createConfiguration(configuration)
        nativeAdLoader.loadAd(nativeAdRequestConfiguration)
    }

    private fun createConfiguration(configuration: NativeAdLoader.Configuration): NativeAdRequestConfiguration {
        return NativeAdRequestConfiguration
            .Builder("demo-native-content-yandex")
            .apply {
                configuration.age?.let { setAge(it.toString()) }
            }.build()
    }
}
