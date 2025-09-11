package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CoreCircularProgressIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        strokeWidth = 20.dp,
        color = CoreColors.secondary.mainColor,
        strokeCap = StrokeCap.Round
    )
}
