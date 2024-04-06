package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd

interface CompositeListener : NativeAdLoader.Listener {
    fun addListener(listener: NativeAdLoader.Listener)

    companion object {
        operator fun invoke(): CompositeListener {
            return object : CompositeListener {
                private val listeners = ArrayDeque<NativeAdLoader.Listener>()
                private val nextListener: NativeAdLoader.Listener?
                    get() = listeners.removeFirstOrNull()

                override fun addListener(listener: NativeAdLoader.Listener) {
                    listeners.add(listener)
                }

                override fun onLoaded(platformNativeAd: PlatformNativeAd) {
                    nextListener?.onLoaded(platformNativeAd)
                }

                override fun onError(description: String) {
                    nextListener?.onError(description)
                }
            }
        }
    }
}
