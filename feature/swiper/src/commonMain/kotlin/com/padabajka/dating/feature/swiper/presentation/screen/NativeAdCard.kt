package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.runtime.Composable
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.feature.swiper.presentation.model.NativeAdItem

@Composable
fun NativeAdCard(nativeAdItem: NativeAdItem) {
    NativeAdCard(nativeAdItem.platformNativeAd)
}

@Composable
expect fun NativeAdCard(platformNativeAd: PlatformNativeAd)
