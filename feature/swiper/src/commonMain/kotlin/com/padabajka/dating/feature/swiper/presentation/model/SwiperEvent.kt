package com.padabajka.dating.feature.swiper.presentation.model

sealed interface SwiperEvent

data class SuperLikeEvent(val cardItem: CardItem, val message: String) : SwiperEvent
data class LikeEvent(val cardItem: CardItem) : SwiperEvent
data class DislikeEvent(val cardItem: CardItem) : SwiperEvent
data object ReturnEvent : SwiperEvent
data class EndOfCardAnimationEvent(val cardItem: CardItem) : SwiperEvent
data object EndReturnLastCardEvent : SwiperEvent

data class ApplySearchPrefEvent(val searchPreferencesItem: SearchPreferencesItem) : SwiperEvent
