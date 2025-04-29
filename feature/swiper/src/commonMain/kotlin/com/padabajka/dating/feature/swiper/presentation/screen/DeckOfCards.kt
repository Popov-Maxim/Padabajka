package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
import com.padabajka.dating.feature.swiper.presentation.model.DislikeEvent
import com.padabajka.dating.feature.swiper.presentation.model.EndOfCardAnimationEvent
import com.padabajka.dating.feature.swiper.presentation.model.LikeEvent
import com.padabajka.dating.feature.swiper.presentation.model.SuperLikeEvent
import com.padabajka.dating.feature.swiper.presentation.screen.card.CardReaction

@Composable
fun DeckOfCards(
    swiperScreenComponent: SwiperScreenComponent
) {
    val state by swiperScreenComponent.state.subscribeAsState()
    val cardDeck = state.cardDeck

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val cards = cardDeck.getCards()
        cards.forEachIndexed { i, card ->
            key(card) {
                AnimationCard(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 10.dp)
                        .zIndex((cards.size - i).toFloat()),
                    content = @Composable { controller ->
                        Card(
                            modifier = Modifier.clip(RoundedCornerShape(50.dp)).fillMaxSize(),
                            cardItem = card
                        ) {
                            when (it) {
                                CardReaction.Dislike -> controller.swipeLeft()
                                CardReaction.Like -> controller.swipeRight()
                                CardReaction.Return -> TODO()
                                CardReaction.SupperLike -> controller.swipeUp()
                            }
                        }
                    },
                    onSwipe = {
                        val reaction = when (it) {
                            Swipe.Left -> DislikeEvent(card)
                            Swipe.Right -> LikeEvent(card)
                            Swipe.Up -> SuperLikeEvent(card)
                        }
                        println("LOG UI: onSwipe $it")

                        swiperScreenComponent.onEvent(reaction)
                    },
                    onEndSwipeAnimation = {
                        swiperScreenComponent.onEvent(EndOfCardAnimationEvent(card))
                    }
                )
            }
        }
    }
}
