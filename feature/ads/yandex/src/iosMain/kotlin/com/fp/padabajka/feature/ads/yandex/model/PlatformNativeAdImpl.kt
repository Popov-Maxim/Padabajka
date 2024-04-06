package com.fp.padabajka.feature.ads.yandex.model

import cocoapods.YandexMobileAds.YMANativeAdProtocol
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
class PlatformNativeAdImpl(
    val nativeAd: YMANativeAdProtocol
) : PlatformNativeAd {
    override fun bind(container: UIView) {
        TODO("Not yet implemented")
    }
}
