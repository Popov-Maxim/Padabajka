package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.runtime.Composable
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent

@Composable
fun SwiperScreen(swiperScreenComponent: SwiperScreenComponent) {
    if (TEST_MODE) {
        TestDeckOfCards()
    } else {
        DeckOfCards(swiperScreenComponent)
    }
}

private const val TEST_MODE = false
