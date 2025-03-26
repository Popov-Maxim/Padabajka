package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt

@Composable
fun DistanceSlider(distanceInKm: Int, updateDistance: (Int) -> Unit) {
    var distance by remember { mutableStateOf(distanceInKm.toFloat()) }
    LaunchedEffect(distanceInKm) {
        distance = distanceInKm.toFloat()
    }
    Slider(
        value = distance,
        onValueChange = {
            distance = it
        },
        valueRange = maxDistanceRange.toFloatRange(),
        onValueChangeFinished = { updateDistance(distance.roundToInt()) }
    )
    Box(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = "${distance.roundToInt()} km"
        )
    }
}

private const val MAX_DISTANCE = 100
private val maxDistanceRange = 0..MAX_DISTANCE
