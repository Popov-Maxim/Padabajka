package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.core.repository.api.model.profile.age
import com.fp.padabajka.feature.ads.data.model.PlatformNativeAdImpl
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class NativeAdRemoteDataStore(private val nativeAdLoader: NativeAdLoader) {
    actual val ads: Flow<PlatformNativeAd>
        get() = nativeAdLoader.asFlow()

    actual suspend fun loadAd(profile: Profile?) {
        val nativeAdRequestConfiguration = createConfiguration(profile)
        nativeAdLoader.loadAd(nativeAdRequestConfiguration)
    }

    private fun createConfiguration(profile: Profile?): NativeAdRequestConfiguration {
        return NativeAdRequestConfiguration
            .Builder("demo-native-content-yandex")
            .apply {
                profile?.age()?.let { setAge(it.toString()) }
            }.build()
    }

    private fun NativeAdLoader.asFlow(): Flow<PlatformNativeAdImpl> {
        return callbackFlow {
            val callback = object : NativeAdLoadListener {
                override fun onAdLoaded(nativeAd: NativeAd) {
                    trySendBlocking(PlatformNativeAdImpl(nativeAd))
                }

                override fun onAdFailedToLoad(p0: AdRequestError) {
                    cancel()
                }
            }

            setNativeAdLoadListener(callback)
            awaitClose {
                setNativeAdLoadListener(null)
            }
        }
    }
}
