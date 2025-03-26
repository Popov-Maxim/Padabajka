package com.padabajka.dating.core.data

import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.repository.api.model.profile.Age

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
