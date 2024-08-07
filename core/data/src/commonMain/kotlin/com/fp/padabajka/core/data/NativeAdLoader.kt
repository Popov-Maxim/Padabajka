package com.fp.padabajka.core.data

import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.core.repository.api.model.profile.Age

interface NativeAdLoader {

    interface Listener {
        fun onLoaded(platformNativeAd: PlatformNativeAd)
        fun onError(description: String)
    }

    data class Configuration(
        val age: Age? = null
    )

    fun setListener(listener: Listener?)

    fun loadAd(configuration: Configuration)
}
