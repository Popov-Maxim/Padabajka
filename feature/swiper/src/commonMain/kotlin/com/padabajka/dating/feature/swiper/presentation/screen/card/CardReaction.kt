package com.padabajka.dating.feature.swiper.presentation.screen.card

sealed interface CardReaction {
    data object Like : CardReaction
    data object SuperLike : CardReaction
    data object Dislike : CardReaction
    data object Return : CardReaction
}
