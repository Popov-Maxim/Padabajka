package com.fp.padabajka.feature.ads.yandex.model

import android.view.View
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.yandex.mobile.ads.nativeads.NativeAd

class PlatformNativeAdImpl(
    val nativeAd: NativeAd
) : PlatformNativeAd {
    override fun bind(container: View) {
        TODO("Not yet implemented")
    }
}
