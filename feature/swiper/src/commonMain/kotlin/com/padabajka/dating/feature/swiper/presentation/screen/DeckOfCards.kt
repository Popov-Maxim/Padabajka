package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.padabajka.dating.core.presentation.ui.font.PlayfairDisplay
import com.padabajka.dating.feature.swiper.presentation.SwiperScreenComponent
import com.padabajka.dating.feature.swiper.presentation.model.ApplySearchPrefEvent
import com.padabajka.dating.feature.swiper.presentation.model.DislikeEvent
import com.padabajka.dating.feature.swiper.presentation.model.EndOfCardAnimationEvent
import com.padabajka.dating.feature.swiper.presentation.model.LikeEvent
import com.padabajka.dating.feature.swiper.presentation.model.PersonItem
import com.padabajka.dating.feature.swiper.presentation.model.SuperLikeEvent
import com.padabajka.dating.feature.swiper.presentation.screen.card.CardReaction
import com.padabajka.dating.feature.swiper.presentation.screen.search.ShearedPrefEditorDialog
import kotlinx.coroutines.launch

@Composable
fun DeckOfCards(swiperScreenComponent: SwiperScreenComponent) {
    val state by swiperScreenComponent.state.subscribeAsState()
    val cardDeck = state.cardDeck
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    Column {
        Box(modifier = Modifier.height(70.dp).fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "PADABAJKA",
                letterSpacing = 36.sp / 10,
                fontSize = 36.sp,
                fontFamily = PlayfairDisplay,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = {
                    scope.launch {
                        sheetState.show()
                    }
                },
                modifier = Modifier
                    .fillMaxHeight().wrapContentWidth()
                    .padding(8.dp).align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                )
            }
        }
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

                            println("LOG: ${reaction::class.simpleName} ${(card as PersonItem).id}")

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
    ShearedPrefEditorDialog(
        sheetState = sheetState,
        searchPreferences = state.searchPreferences,
        applyDiff = { newSearchPref -> swiperScreenComponent.onEvent(ApplySearchPrefEvent(newSearchPref)) }
    )
}
