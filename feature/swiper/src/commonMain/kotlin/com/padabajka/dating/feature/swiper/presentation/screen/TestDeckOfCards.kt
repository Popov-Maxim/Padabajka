package com.padabajka.dating.feature.swiper.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.swiper.PersonId
import com.padabajka.dating.feature.swiper.presentation.model.PersonItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

@Suppress("LongMethod")
@Composable
fun TestDeckOfCards() {
    var index by remember { mutableStateOf(0) }
    var cardDeck: CardDeck by remember {
        mutableStateOf(CardDeck())
    }
    LaunchedEffect(Unit) {
        for (i in 0..START_CARD_COUNT) {
            index++
            cardDeck = cardDeck.add(createPersonItem(i))
        }
        while (false) {
            cardDeck = cardDeck.add(createPersonItem(index++))
            delay(timeMillis = 5000)
        }
    }

    val time1 = System.now()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val cards = cardDeck.getCards()
        cards.forEachIndexed { i, card ->
            key(card) {
                AnimationCard(
                    modifier = Modifier.height(300.dp).width(200.dp)
                        .zIndex((cards.size - i).toFloat()),
                    content = @Composable {
                        Card(cardItem = card) {}
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier.size(5.dp)
                                    .background(Color.Red.copy(alpha = 0.1f)),
                            )
                        }
                    },
                    onSwipe = {
                        cardDeck = cardDeck.add(createPersonItem(index++))
                    },
                    onEndSwipeAnimation = {
                        cardDeck = cardDeck.remove(card)
                    }
                )
            }
        }
        with(LocalDensity.current) {
            Box(
                modifier = Modifier.zIndex(zIndex = 100f)
                    .width(300.toDp() * 2).height(400.toDp() * 2)
                    .background(Color.Red.copy(alpha = 0.1F))
            )
        }
    }
    val time = System.now() - time1

    Text(
        text = time.toString()
    )
}

private fun createPersonItem(index: Int): PersonItem {
    return PersonItem(
        id = PersonId("index"),
        name = "Name $index",
        age = Age.minAge,
        images = persistentListOf(),
        aboutMe = "aboutMe $index",
        details = persistentListOf(),
        mainAchievement = null,
        achievements = persistentListOf(),
        lookingFor = LookingForData.default,
        lifestyles = persistentListOf()
    )
}

object System {
    fun now(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}

private const val START_CARD_COUNT = 3
