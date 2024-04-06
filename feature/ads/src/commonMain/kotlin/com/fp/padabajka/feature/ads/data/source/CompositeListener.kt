package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd

class CompositeListener : NativeAdLoader.Listener {

    private val listeners = ArrayDeque<NativeAdLoader.Listener>()
    private val nextListener: NativeAdLoader.Listener?
        get() = listeners.removeFirstOrNull()

    fun addListener(listener: NativeAdLoader.Listener) {
        listeners.add(listener)
    }

    override fun onLoaded(platformNativeAd: PlatformNativeAd) {
        nextListener?.onLoaded(platformNativeAd)
    }

    override fun onError(description: String) {
        nextListener?.onError(description)
    }
}
