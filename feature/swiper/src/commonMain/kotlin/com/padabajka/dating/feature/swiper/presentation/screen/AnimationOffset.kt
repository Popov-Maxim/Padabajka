package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.ui.geometry.Offset

data class AnimationOffset(
    val offset: Offset,
    val needAnimate: Boolean = false,
    val finishedListener: ((Offset) -> Unit)? = null
) {
    operator fun plus(other: Offset): AnimationOffset {
        return AnimationOffset(offset + other, needAnimate, finishedListener)
    }

    companion object {
        val Zero = AnimationOffset(Offset.Zero)
    }
}
