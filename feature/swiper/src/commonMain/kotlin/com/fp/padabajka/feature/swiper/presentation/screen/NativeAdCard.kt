package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.runtime.Composable
import com.fp.padabajka.core.repository.api.model.ads.PlatformNativeAd
import com.fp.padabajka.feature.swiper.presentation.model.NativeAdItem

@Composable
fun NativeAdCard(nativeAdItem: NativeAdItem) {
    NativeAdCard(nativeAdItem.platformNativeAd)
}

@Composable
expect fun NativeAdCard(platformNativeAd: PlatformNativeAd)
