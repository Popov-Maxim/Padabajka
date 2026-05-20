package com.padabajka.dating.feature.swiper.presentation.screen

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.feature.swiper.presentation.model.CardPosition

@Composable
actual fun NativeAdCard(
    modifier: Modifier,
    platformNativeAd: PlatformNativeAd,
    cardPosition: CardPosition
) {
    Box(
        modifier = modifier.fillMaxSize()
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
