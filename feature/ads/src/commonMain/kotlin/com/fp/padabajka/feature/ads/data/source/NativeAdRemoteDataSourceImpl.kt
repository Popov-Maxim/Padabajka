package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Profile
import com.fp.padabajka.core.repository.api.model.profile.age
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NativeAdRemoteDataSourceImpl(
    private val nativeAdLoader: NativeAdLoader,
    private val compositeListener: CompositeListener
) : NativeAdRemoteDataSource {

    init {
        nativeAdLoader.setListener(compositeListener)
    }

    override suspend fun loadAd(profile: Profile?): PlatformNativeAd {
        val configuration = NativeAdLoader.Configuration(
            age = profile?.age()
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
