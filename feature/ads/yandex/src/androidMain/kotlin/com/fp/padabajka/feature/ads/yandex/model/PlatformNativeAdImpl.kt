package com.fp.padabajka.feature.ads.yandex.model

import android.view.ViewGroup
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.template.NativeBannerView

class PlatformNativeAdImpl(
    private val nativeAd: NativeAd
) : PlatformNativeAd {
    override fun bind(container: ViewGroup) {
        val context = container.context
        val nativeBannerView = NativeBannerView(context)
        container.addView(nativeBannerView)

        nativeBannerView.setAd(nativeAd)
    }
}
