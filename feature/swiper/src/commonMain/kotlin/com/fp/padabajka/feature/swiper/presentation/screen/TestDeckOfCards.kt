package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
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
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import com.fp.padabajka.feature.swiper.presentation.model.PersonItem
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

@Composable
fun TestDeckOfCards() {
    var index by remember { mutableStateOf(0) }
    var arrayDeque: SafePersistentList<CardItem> by remember {
        mutableStateOf(persistentListOf<CardItem>().toSafe())
    }
    LaunchedEffect(Unit) {
        for (i in 0..START_CARD_COUNT) {
            index++
            arrayDeque = arrayDeque.add(createPersonItem(i))
        }
        while (false) {
            arrayDeque = arrayDeque.add(createPersonItem(index++))
            delay(timeMillis = 5000)
        }
    }

    val time1 = System.now()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        arrayDeque.getResult().forEachIndexed { i, card ->
            key(card) {
                AnimationCard(
                    modifier = Modifier.height(300.dp).width(200.dp)
                        .zIndex((arrayDeque.size - i).toFloat()),
                    content = @Composable {
                        Card(card)
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
                        arrayDeque = arrayDeque.add(createPersonItem(index++))
                    },
                    onEndSwipeAnimation = {
                        arrayDeque = arrayDeque.remove(card)
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
        firstName = "firstName $index",
        lastName = "lastName $index",
        birthday = LocalDate(year = 1990, monthNumber = 1, dayOfMonth = 1),
        images = persistentListOf(),
        aboutMe = "aboutMe $index",
        details = persistentListOf(),
        mainAchievement = null,
        achievements = persistentListOf()
    )
}

private object System {
    fun now(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
}

private const val START_CARD_COUNT = 3
