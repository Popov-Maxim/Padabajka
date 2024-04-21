package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.feature.swiper.presentation.SwiperScreenComponent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent

@Composable
fun SwiperScreen(swiperScreenComponent: SwiperScreenComponent) {
    swiperScreenComponent.onEvent(LikeEvent(PersonId(1)))
    Text(
        text = "SwiperScreen"
    )
}
