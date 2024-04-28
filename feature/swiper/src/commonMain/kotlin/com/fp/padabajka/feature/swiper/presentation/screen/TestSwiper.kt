package com.fp.padabajka.feature.swiper.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TestSwiper() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        var index by remember { mutableStateOf(0) }
        val foregroundContent: @Composable () -> Unit = {
            TestCard(index)
        }
        val betweenContent: @Composable (Swipe?) -> Unit = {
            if (it != null) {
                val color = when (it) {
                    Swipe.Left -> Color.Red
                    Swipe.Right -> Color.Green
                    Swipe.Up -> Color.Blue
                }
                Box(
                    modifier = Modifier.fillMaxSize().background(color.copy(alpha = 0.5f)),
                )
            }
        }
        val backgroundContent: @Composable () -> Unit = {
            TestCard(index + 1)
        }
        SwipeCard(
            modifier = Modifier.height(300.dp).width(200.dp),
            onSwipe = { index++ },
            swipeHorizontalThreshold = 200f,
            foregroundContent = foregroundContent,
            betweenContent = betweenContent,
            backgroundContent = backgroundContent
        )
    }
}

@Composable
private fun TestCard(index: Int) {
    Column(
        modifier = Modifier.fillMaxSize().background(
            if (index % 2 == 0) Color.Cyan else Color.LightGray
        ),
    ) {
        Text(
            text = "Page: $index",
            textAlign = TextAlign.Center,
        )
    }
}
