package com.padabajka.dating.feature.ads.yandex.model

import cocoapods.YandexMobileAds.YMANativeAdProtocol
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.feature.ads.yandex.ui.CustomNativeAdView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIStackView

@OptIn(ExperimentalForeignApi::class)
class PlatformNativeAdImpl(
    private val nativeAd: YMANativeAdProtocol
) : PlatformNativeAd {
    override fun bind(container: UIStackView) {
        val bannerView = CustomNativeAdView()
        container.addArrangedSubview(bannerView)

        NSLayoutConstraint.activateConstraints(
            listOf(
                bannerView.topAnchor.constraintEqualToAnchor(container.topAnchor),
                bannerView.leadingAnchor.constraintEqualToAnchor(container.leadingAnchor),
                bannerView.trailingAnchor.constraintEqualToAnchor(container.trailingAnchor),
                bannerView.bottomAnchor.constraintEqualToAnchor(container.bottomAnchor)
            )
        )

        nativeAd.bindWithAdView(bannerView, null)
    }
}
