package com.padabajka.dating.feature.ads.data.source

import com.padabajka.dating.core.data.NativeAdLoader
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class NativeAdRemoteDataSourceImpl(
    private val nativeAdLoader: NativeAdLoader,
    private val compositeListener: CompositeListener
) : NativeAdRemoteDataSource {

    init {
        nativeAdLoader.setListener(compositeListener)
    }

    override suspend fun loadAd(profile: Profile?): PlatformNativeAd {
        val configuration = NativeAdLoader.Configuration(
            age = profile?.age
        )
        return suspendCoroutine {
            compositeListener.addListener(object : NativeAdLoader.Listener {
                override fun onLoaded(platformNativeAd: PlatformNativeAd) {
                    it.resume(platformNativeAd)
                }

                override fun onError(description: String) {
                    it.resumeWithException(LoadErrorException(description))
                }
            })
            nativeAdLoader.loadAd(configuration)
        }
    }
}
