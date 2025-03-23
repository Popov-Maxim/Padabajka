package com.fp.padabajka.feature.swiper.presentation.screen.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.fp.padabajka.feature.swiper.presentation.screen.Swipe

interface CardController {
    fun swipeLeft()
    fun swipeRight()
    fun swipeUp()
}

@Composable
fun rememberCardController(
    swipe: (Swipe) -> Unit
) = remember {
    object : CardController {
        override fun swipeLeft() {
            swipe(Swipe.Left)
        }

        override fun swipeRight() {
            swipe(Swipe.Right)
        }

        override fun swipeUp() {
            swipe(Swipe.Up)
        }
    }
}
