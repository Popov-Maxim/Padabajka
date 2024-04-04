package com.fp.padabajka.core.data

import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd

interface NativeAdLoader {

    interface Listener {
        fun onLoaded(platformNativeAd: PlatformNativeAd)
        fun onError()
    }

    data class Configuration(
        val age: Int? = null
    )

    fun setListener(listener: Listener?)

    fun loadAd(configuration: Configuration)
}
