package com.padabajka.dating.core.utils

object FeatureToggle {
    val graphicsLayerCard = platform == Platform.Android

    val forceAd = isDebugBuild && false
}
