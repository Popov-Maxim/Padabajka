package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.feature.swiper.presentation.SwiperScreenComponent
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent

@Composable
fun SwiperScreen(swiperScreenComponent: SwiperScreenComponent) {
    val swiperState by swiperScreenComponent.state.subscribeAsState()
    val onSwipe: (Swipe) -> Unit = { swipe ->
        val personId = PersonId(1)
        when (swipe) {
            Swipe.Left -> swiperScreenComponent.onEvent(DislikeEvent(personId))
            Swipe.Right -> swiperScreenComponent.onEvent(LikeEvent(personId))
            Swipe.Up -> swiperScreenComponent.onEvent(SuperLikeEvent(personId))
        }
    }
    CardSwiper(swiperState, onSwipe)
}
