package com.fp.padabajka.feature.swiper.presentation.screen.card

sealed interface CardReaction {
    data object Like : CardReaction
    data object SupperLike : CardReaction
    data object Dislike : CardReaction
    data object Return : CardReaction
}
