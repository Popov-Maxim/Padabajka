package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.fp.padabajka.feature.swiper.presentation.SwiperScreenComponent
import com.fp.padabajka.feature.swiper.presentation.model.ApplySearchPrefEvent
import com.fp.padabajka.feature.swiper.presentation.model.DislikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.EndOfCardAnimationEvent
import com.fp.padabajka.feature.swiper.presentation.model.LikeEvent
import com.fp.padabajka.feature.swiper.presentation.model.SuperLikeEvent
import com.fp.padabajka.feature.swiper.presentation.screen.search.ShearedPrefEditorDialog
import com.fp.padabajka.feature.swiper.presentation.screen.setting.AppSettingsDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeckOfCards(swiperScreenComponent: SwiperScreenComponent) {
    val state by swiperScreenComponent.state.subscribeAsState()
    val cardDeck = state.cardDeck
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.Gray),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                scope.launch {
                    sheetState.show()
                }
            }) {
                Text(text = "Settings")
            }

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxHeight().wrapContentWidth()
                    .padding(8.dp)
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
                            .padding(horizontal = 30.dp, vertical = 100.dp)
                            .zIndex((cards.size - i).toFloat()),
                        content = @Composable {
                            Card(card)
                        },
                        onSwipe = {
                            val reaction = when (it) {
                                Swipe.Left -> DislikeEvent(card)
                                Swipe.Right -> LikeEvent(card)
                                Swipe.Up -> SuperLikeEvent(card)
                            }

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

    if (showDialog) {
        AppSettingsDialog { showDialog = false }
    }
}
