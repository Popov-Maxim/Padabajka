package com.padabajka.dating.feature.swiper.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

@Immutable
data class CardPosition(
    val rotationZ: Float,
    val offset: Offset,
)
