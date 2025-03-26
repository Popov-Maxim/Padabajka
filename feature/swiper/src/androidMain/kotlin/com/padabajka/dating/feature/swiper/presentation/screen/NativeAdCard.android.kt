package com.padabajka.dating.feature.swiper.presentation.screen

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd

@Composable
actual fun NativeAdCard(platformNativeAd: PlatformNativeAd) {
    Box(
        modifier = Modifier.fillMaxSize().border(1.dp, Color.Black)
    ) {
        AndroidView(
            factory = {
                val rootView = FrameLayout(it).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                platformNativeAd.bind(rootView)
                rootView
            }
        )
    }
}
