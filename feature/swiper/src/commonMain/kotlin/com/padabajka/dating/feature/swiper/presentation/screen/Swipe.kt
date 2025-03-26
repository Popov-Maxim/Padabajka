package com.padabajka.dating.feature.swiper.presentation.screen

sealed interface Swipe {
    data object Left : Swipe
    data object Right : Swipe
    data object Up : Swipe
}
