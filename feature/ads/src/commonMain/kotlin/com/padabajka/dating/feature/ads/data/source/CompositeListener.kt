package com.padabajka.dating.feature.ads.data.source

import com.padabajka.dating.core.data.NativeAdLoader
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd

internal interface CompositeListener : NativeAdLoader.Listener {
    fun addListener(listener: NativeAdLoader.Listener)

    companion object {
        operator fun invoke(): CompositeListener = CompositeListenerImpl()
    }
}

private class CompositeListenerImpl : CompositeListener {
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
