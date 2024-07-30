package com.fp.padabajka.feature.swiper.presentation.model

sealed interface SwiperEvent

data class SuperLikeEvent(val cardItem: CardItem) : SwiperEvent
data class LikeEvent(val cardItem: CardItem) : SwiperEvent
data class DislikeEvent(val cardItem: CardItem) : SwiperEvent
data class EndOfCardAnimationEvent(val cardItem: CardItem) : SwiperEvent

data class UpdateEditSearchPrefEvent(
    val update: (SearchPreferencesItem) -> SearchPreferencesItem.Success
) : SwiperEvent
data class ApplySearchPrefEvent(val searchPreferencesItem: SearchPreferencesItem) : SwiperEvent
data object ResetSearchPrefEvent : SwiperEvent
