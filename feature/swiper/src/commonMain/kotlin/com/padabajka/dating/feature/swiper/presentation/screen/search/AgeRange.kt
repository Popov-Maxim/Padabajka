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
import com.padabajka.dating.core.repository.api.model.profile.AgeRange
import com.padabajka.dating.core.repository.api.model.profile.rangeTo
import com.padabajka.dating.core.repository.api.model.profile.toAge
import com.padabajka.dating.feature.swiper.presentation.model.SearchPreferencesConstants
import kotlin.math.roundToInt

@Composable
fun AgeRange(ageRange: AgeRange, update: (AgeRange) -> Unit) {
    val ageFloatRange = ageRange.toFloatRange()
    var sliderPosition by remember { mutableStateOf(ageFloatRange) }
    LaunchedEffect(ageFloatRange) {
        sliderPosition = ageFloatRange
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "${StaticTextId.UiId.Age.translate()}:",
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
    val colors = SliderDefaults.colors(
        thumbColor = CoreColors.secondary.mainColor,
        activeTrackColor = CoreColors.secondary.mainColor,
    )
    CustomRangeSlider(
        value = sliderPosition,
        onValueChange = { range ->
            sliderPosition = range
        },
        valueRange = SearchPreferencesConstants.maxAgeRange.toFloatRange(),
        onValueChangeFinished = { update.invoke(sliderPosition.toAgeRange()) },
        colors = colors
    )
}

private fun AgeRange.toFloatRange(): ClosedFloatingPointRange<Float> =
    start.raw.toFloat()..endInclusive.raw.toFloat()

private fun ClosedFloatingPointRange<Float>.toAgeRange(): AgeRange =
    start.roundToInt().toAge()..endInclusive.roundToInt().toAge()
