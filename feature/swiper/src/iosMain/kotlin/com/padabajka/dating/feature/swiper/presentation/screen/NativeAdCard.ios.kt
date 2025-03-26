package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIStackView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeAdCard(platformNativeAd: PlatformNativeAd) {
    Box(
        modifier = Modifier.fillMaxSize().border(1.dp, Color.Black)
    ) {
        UIKitView(
            factory = {
                UIStackView().apply {
                    platformNativeAd.bind(this)
                }
            },
            modifier = Modifier.fillMaxSize().padding(bottom = 100.dp)
        )
    }
}
