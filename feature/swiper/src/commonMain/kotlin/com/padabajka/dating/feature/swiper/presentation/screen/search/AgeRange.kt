package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
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
import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import com.padabajka.dating.core.repository.api.model.profile.rangeTo
import com.padabajka.dating.core.repository.api.model.profile.toAge
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AgeRange(ageRange: AgeRange, update: (AgeRange) -> Unit) {
    val ageFloatRange = ageRange.toFloatRange()
    var sliderPosition by remember { mutableStateOf(ageFloatRange) }
    LaunchedEffect(ageFloatRange) {
        sliderPosition = ageFloatRange
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Age:",
            modifier = Modifier.align(Alignment.CenterStart)
        )

        val minAge = sliderPosition.start.roundToInt()
        val maxAge = sliderPosition.endInclusive.roundToInt()

        val ageValues = "$minAge - $maxAge"
        Text(
            text = ageValues,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
    RangeSlider(
        value = sliderPosition,
        onValueChange = { range ->
            sliderPosition = range
        },
        valueRange = maxAgeRange.toFloatRange(),
        onValueChangeFinished = { update.invoke(sliderPosition.toAgeRange()) },
        colors = SliderDefaults.colors(
            thumbColor = CoreColors.secondary.mainColor,
            activeTrackColor = CoreColors.secondary.mainColor,
        )
    )
}

private fun AgeRange.toFloatRange(): ClosedFloatingPointRange<Float> =
    start.raw.toFloat()..endInclusive.raw.toFloat()

fun IntRange.toFloatRange(): ClosedFloatingPointRange<Float> =
    start.toFloat()..endInclusive.toFloat()

private fun ClosedFloatingPointRange<Float>.toAgeRange(): AgeRange =
    start.roundToInt().toAge()..endInclusive.roundToInt().toAge()

private val maxAgeRange: IntRange = Age.minAge.raw..Age.maxAge.raw
