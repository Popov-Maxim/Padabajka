package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.feature.swiper.presentation.model.SearchPreferencesConstants
import kotlin.math.roundToInt

@Composable
fun DistanceSlider(distanceInKm: Int, updateDistance: (Int) -> Unit) {
    var distance by remember { mutableStateOf(distanceInKm.toFloat()) }
    LaunchedEffect(distanceInKm) {
        distance = distanceInKm.toFloat()
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${StaticTextId.UiId.Distance.translate()}:",
            modifier = Modifier.align(Alignment.CenterStart)
        )
        val ageValues = "${distance.roundToInt()} ${StaticTextId.UiId.Km.translate()}"
        Text(
            text = ageValues,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
    CustomSlider(
        value = distance,
        onValueChange = {
            distance = it
        },
        valueRange = SearchPreferencesConstants.maxDistanceRange.toFloatRange(),
        colors = SliderDefaults.colors(
            thumbColor = CoreColors.secondary.mainColor,
            activeTrackColor = CoreColors.secondary.mainColor,
        ),
        onValueChangeFinished = { updateDistance(distance.roundToInt()) }
    )
}

fun IntRange.toFloatRange(): ClosedFloatingPointRange<Float> =
    start.toFloat()..endInclusive.toFloat()
