package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fp.padabajka.feature.swiper.presentation.model.CardItem
import com.fp.padabajka.feature.swiper.presentation.model.EmptyCardItem
import com.fp.padabajka.feature.swiper.presentation.model.LoadingItem
import com.fp.padabajka.feature.swiper.presentation.model.NativeAdItem
import com.fp.padabajka.feature.swiper.presentation.model.PersonItem

@Composable
fun Card(cardItem: CardItem) {
    when (cardItem) {
        EmptyCardItem -> {}
        LoadingItem -> {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                )
            }
        }
        is NativeAdItem -> {}
        is PersonItem -> PersonCard(cardItem)
    }
}

@Composable
private fun PersonCard(personItem: PersonItem) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Gray)
            .border(1.dp, Color.Black)
    ) {
        Text(
            text = "firstName: ${personItem.firstName}",
            textAlign = TextAlign.Center,
        )
        Text(
            text = "lastName: ${personItem.lastName}",
            textAlign = TextAlign.Center,
        )
        Text(
            text = "lastName: ${personItem.birthday}",
            textAlign = TextAlign.Center,
        )
    }
}
