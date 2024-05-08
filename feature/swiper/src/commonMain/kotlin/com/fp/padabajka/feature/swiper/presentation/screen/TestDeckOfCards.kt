package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fp.padabajka.core.repository.api.model.swiper.PersonId
import com.fp.padabajka.feature.swiper.presentation.model.PersonItem
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate

@Composable
fun TestDeckOfCards() {
    var index by remember { mutableStateOf(0) }
    var arrayDeque: PersistentList<AnimationCard> by remember {
        mutableStateOf(persistentListOf())
    }
    LaunchedEffect(Unit) {
        while (true) {
            arrayDeque = arrayDeque.add(0, createAnimationCard(index) { })
            index++
            delay(timeMillis = 5000)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        arrayDeque.forEach {
            it.draw()
        }
    }
}

private fun createAnimationCard(index: Int, onSwipe: (Swipe) -> Unit): AnimationCard {
    val card = createPersonItem(index)
    val cardDesign: @Composable () -> Unit = { Card(card) }
    return AnimationCard(
        Modifier.height(300.dp).width(200.dp),
        cardDesign,
        onSwipe
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
