package com.padabajka.dating.feature.ads.data.source

import com.padabajka.dating.core.data.NativeAdLoader
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Profile
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.seconds

internal class NativeAdRemoteDataSourceImpl(
    private val nativeAdLoader: NativeAdLoader,
    private val compositeListener: CompositeListener
) : NativeAdRemoteDataSource {

    init {
        nativeAdLoader.setListener(compositeListener)
    }

    override suspend fun loadAd(profile: Profile?): PlatformNativeAd? {
        val configuration = NativeAdLoader.Configuration(
            age = profile?.age
        )

        return withTimeoutOrNull(5.seconds) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : NativeAdLoader.Listener {
                    override fun onLoaded(platformNativeAd: PlatformNativeAd) {
                        continuation.resume(platformNativeAd)
                    }

                    override fun onError(description: String) {
                        continuation.resume(null)
                    }
                }

                continuation.invokeOnCancellation {
                    compositeListener.removeListener(listener)
                }
                compositeListener.addListener(listener)
                nativeAdLoader.loadAd(configuration)
            }
        }
    }
}
