package com.fp.padabajka.feature.ads.data.source

import cocoapods.YandexMobileAds.YMANativeAdLoader
import cocoapods.YandexMobileAds.YMANativeAdLoaderDelegateProtocol
import cocoapods.YandexMobileAds.YMANativeAdProtocol
import cocoapods.YandexMobileAds.YMANativeAdRequestConfiguration
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.core.repository.api.model.profile.age
import com.fp.padabajka.feature.ads.data.model.PlatformNativeAdImpl
import com.fp.padabajka.feature.ads.data.toNSNumber
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Foundation.NSError
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
actual class NativeAdRemoteDataStore(
    private val nativeAdLoader: YMANativeAdLoader
) {
    actual val ads: Flow<PlatformNativeAd>
        get() = nativeAdLoader.asFlow()

    actual suspend fun loadAd(profile: Profile?) {
        val requestConfiguration = YMANativeAdRequestConfiguration("demo-native-content-yandex")
            .mutableConfiguration().apply {
                setAge(profile?.age()?.toNSNumber())
            }

        nativeAdLoader.loadAdWithRequestConfiguration(requestConfiguration)
    }

    private fun YMANativeAdLoader.asFlow(): Flow<PlatformNativeAd> {
        return callbackFlow {
            val callback = object : UIViewController(), YMANativeAdLoaderDelegateProtocol {
                override fun nativeAdLoader(
                    loader: YMANativeAdLoader,
                    didFailLoadingWithError: NSError
                ) {
                    cancel()
                }
                override fun nativeAdLoader(
                    loader: YMANativeAdLoader,
                    didLoadAd: YMANativeAdProtocol
                ) {
                    trySendBlocking(PlatformNativeAdImpl(didLoadAd))
                }
            }

            setDelegate(callback)
            awaitClose {
                setDelegate(null)
            }
        }
    }
}
