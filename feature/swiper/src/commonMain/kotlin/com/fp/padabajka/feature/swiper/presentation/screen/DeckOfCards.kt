package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.swiper.presentation.SwiperScreenComponent
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.EndOfCardAnimationEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.PersonItem
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent

@Composable
fun DeckOfCards(swiperScreenComponent: SwiperScreenComponent) {
    val state by swiperScreenComponent.state.subscribeAsState()
    val cardDeck = state.cardDeck

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        cardDeck.getResult().forEachIndexed { i, card ->
            key(card) {
                AnimationCard(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 30.dp, vertical = 100.dp)
                        .zIndex((cardDeck.size - i).toFloat()),
                    content = @Composable {
                        Card(card)
                    },
                    onSwipe = {
                        if (card is PersonItem) {
                            val personId = card.id
                            val reaction = when (it) {
                                Swipe.Left -> DislikeEvent(personId)
                                Swipe.Right -> LikeEvent(personId)
                                Swipe.Up -> SuperLikeEvent(personId)
                            }

                            swiperScreenComponent.onEvent(reaction)
                        }
                    },
                    onEndSwipeAnimation = {
                        swiperScreenComponent.onEvent(EndOfCardAnimationEvent(card))
                    }
                )
            }
        }
    }
}
