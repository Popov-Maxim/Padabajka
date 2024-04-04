package com.fp.padabajka.feature.ads.yandex

import cocoapods.YandexMobileAds.YMANativeAdLoader
import cocoapods.YandexMobileAds.YMANativeAdLoaderDelegateProtocol
import cocoapods.YandexMobileAds.YMANativeAdProtocol
import cocoapods.YandexMobileAds.YMANativeAdRequestConfiguration
import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.feature.ads.yandex.model.PlatformNativeAdImpl
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
actual class YandexNativeAdLoader(
    private val nativeAdLoader: YMANativeAdLoader
) : NativeAdLoader {

    private class ProxyListener(
        private val listener: NativeAdLoader.Listener
    ) : UIViewController(), YMANativeAdLoaderDelegateProtocol {
        override fun nativeAdLoader(loader: YMANativeAdLoader, didFailLoadingWithError: NSError) {
            listener.onError()
        }

        override fun nativeAdLoader(loader: YMANativeAdLoader, didLoadAd: YMANativeAdProtocol) {
            listener.onLoaded(PlatformNativeAdImpl(didLoadAd))
        }
    }

    override fun setListener(listener: NativeAdLoader.Listener?) {
        nativeAdLoader.setDelegate(listener?.let { ProxyListener(it) })
    }

    override fun loadAd(configuration: NativeAdLoader.Configuration) {
        val requestConfiguration = YMANativeAdRequestConfiguration("demo-native-content-yandex")
            .mutableConfiguration().apply {
                setAge(configuration.age?.toNSNumber())
            }

        nativeAdLoader.loadAdWithRequestConfiguration(requestConfiguration)
    }
}
