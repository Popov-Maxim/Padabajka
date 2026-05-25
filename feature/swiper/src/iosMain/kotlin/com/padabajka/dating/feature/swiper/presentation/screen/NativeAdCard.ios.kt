package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import com.padabajka.dating.core.repository.api.model.ads.PlatformNativeAd
import com.padabajka.dating.core.utils.FeatureToggle
import com.padabajka.dating.feature.swiper.presentation.model.CardPosition
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGAffineTransformMakeRotation
import platform.CoreGraphics.CGAffineTransformTranslate
import platform.UIKit.UIStackView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeAdCard(
    modifier: Modifier,
    platformNativeAd: PlatformNativeAd,
    cardPosition: CardPosition
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        UIKitView(
            factory = {
                UIStackView().apply {
                    platformNativeAd.bind(this)
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                if (FeatureToggle.graphicsLayerCard) {
                    val radians = cardPosition.rotationZ.toDouble() * kotlin.math.PI / 180f
                    val rotation = CGAffineTransformMakeRotation(radians)

                    val offset = cardPosition.offset
                    view.clipsToBounds = true
                    view.transform = CGAffineTransformTranslate(
                        rotation,
                        0.0, 0.0
//                    offset.x.toDouble(),
//                    offset.y.toDouble()
                    )
                }
            },
        )
    }
}
