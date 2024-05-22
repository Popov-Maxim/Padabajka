package com.fp.padabajka.feature.swiper.presentation.model

import com.fp.padabajka.core.repository.api.model.swiper.PersonId

sealed interface SwiperEvent

data class SuperLikeEvent(val personId: PersonId) : SwiperEvent
data class LikeEvent(val personId: PersonId) : SwiperEvent
data class DislikeEvent(val personId: PersonId) : SwiperEvent
data class EndOfCardAnimationEvent(val cardItem: CardItem) : SwiperEvent
