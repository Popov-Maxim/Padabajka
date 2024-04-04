package com.fp.padabajka.feature.ads.data.source

import com.fp.padabajka.core.data.NativeAdLoader
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd

class CompositeListener : NativeAdLoader.Listener {

    private val listeners = ArrayDeque<NativeAdLoader.Listener>()

    fun addListener(listener: NativeAdLoader.Listener) {
        listeners.add(listener)
    }

    override fun onLoaded(platformNativeAd: PlatformNativeAd) {
        listeners.removeFirst().onLoaded(platformNativeAd)
    }

    override fun onError() {
        listeners.removeFirst().onError()
    }
}
