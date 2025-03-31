package com.padabajka.dating.feature.swiper.presentation.screen.search

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (range: ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChangeFinished: (() -> Unit),
    colors: SliderColors
) {
    val thumb: @Composable (RangeSliderState) -> Unit = {
        Thumb(colors)
    }
    val track: @Composable (RangeSliderState) -> Unit = {
        SliderDefaults.Track(
            modifier = Modifier.height(trackHeight),
            rangeSliderState = it,
            colors = colors,
            drawStopIndicator = null
        )
    }

    RangeSlider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        startThumb = thumb,
        endThumb = thumb,
        track = track,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChangeFinished: (() -> Unit),
    colors: SliderColors,
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        thumb = { Thumb(colors) },
        track = {
            SliderDefaults.Track(
                modifier = Modifier.height(trackHeight),
                sliderState = it,
                colors = colors,
                drawStopIndicator = null
            )
        }
    )
}

@Composable
private fun Thumb(
    colors: SliderColors,
    modifier: Modifier = Modifier.size(16.dp)
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = colors.thumbColor,
            radius = size.minDimension / 2,
            style = Fill
        )
    }
}

private val trackHeight = 5.dp
