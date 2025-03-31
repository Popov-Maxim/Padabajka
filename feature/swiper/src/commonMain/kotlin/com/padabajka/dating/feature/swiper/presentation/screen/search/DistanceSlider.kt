package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.mainColor
import kotlin.math.roundToInt

@Composable
fun DistanceSlider(distanceInKm: Int, updateDistance: (Int) -> Unit) {
    var distance by remember { mutableStateOf(distanceInKm.toFloat()) }
    LaunchedEffect(distanceInKm) {
        distance = distanceInKm.toFloat()
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Distance:",
            modifier = Modifier.align(Alignment.CenterStart)
        )
        val ageValues = "${distance.roundToInt()} km"
        Text(
            text = ageValues,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
    Slider(
        value = distance,
        onValueChange = {
            distance = it
        },
        valueRange = maxDistanceRange.toFloatRange(),
        colors = SliderDefaults.colors(
            thumbColor = CoreColors.secondary.mainColor,
            activeTrackColor = CoreColors.secondary.mainColor,
        ),
        onValueChangeFinished = { updateDistance(distance.roundToInt()) }
    )
}

private const val MAX_DISTANCE = 100
private val maxDistanceRange = 0..MAX_DISTANCE
