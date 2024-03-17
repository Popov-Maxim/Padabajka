package com.fp.padabajka.feature.swiper.presentation

import com.arkivanov.decompose.ComponentContext
import com.fp.padabajka.core.presentation.BaseComponent
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SupperLikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperEvent
import com.fp.padabajka.feature.swiper.presentation.model.SwiperState

class SwiperScreenComponent(
    context: ComponentContext
) : BaseComponent<SwiperState>(
    context,
    TODO()
) {
    fun onEvent(event: SwiperEvent) {
        when (event) {
            DislikeEvent -> TODO()
            LikeEvent -> TODO()
            SupperLikeEvent -> TODO()
        }
    }
}
