package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.feature.swiper.presentation.model.CardPosition
import com.padabajka.dating.feature.swiper.presentation.model.NativeAdItem

@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier,
    nativeAdItem: NativeAdItem,
    cardPosition: CardPosition,
) {
    NativeAdCard(modifier, nativeAdItem.platformNativeAd, cardPosition)
}

@Composable
expect fun NativeAdCard(
    modifier: Modifier = Modifier,
    platformNativeAd: PlatformNativeAd,
    cardPosition: CardPosition
)
