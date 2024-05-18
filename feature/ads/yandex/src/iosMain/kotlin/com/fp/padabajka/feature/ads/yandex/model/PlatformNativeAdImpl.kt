package com.fp.padabajka.feature.ads.yandex.model

import cocoapods.YandexMobileAds.YMANativeAdProtocol
import cocoapods.YandexMobileAds.YMANativeBannerView
import cocoapods.YandexMobileAds.YMANativeTemplateAppearance
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIColor
import platform.UIKit.UIStackView

@OptIn(ExperimentalForeignApi::class)
class PlatformNativeAdImpl(
    private val nativeAd: YMANativeAdProtocol
) : PlatformNativeAd {
    override fun bind(container: UIStackView) {
        val bannerView = YMANativeBannerView()
        val appearance = YMANativeTemplateAppearance().mutableAppearance().apply {
            setBackgroundColor(UIColor.grayColor)
        }
        bannerView.applyAppearance(appearance)

        bannerView.setAd(nativeAd)
        container.addArrangedSubview(bannerView)
    }
}
