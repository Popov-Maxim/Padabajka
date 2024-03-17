package com.fp.padabajka.feature.swiper.presentation.model

sealed interface SwiperEvent

data object SupperLikeEvent : SwiperEvent
data object LikeEvent : SwiperEvent
data object DislikeEvent : SwiperEvent
