package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CoreCallToActionButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    val optionalClickable: Modifier.() -> Modifier =
        { if (enabled) clickable(onClick = onClick) else this }
    val buttonColor = CoreColors.secondary.mainColor.copy(
        alpha = if (enabled) 1f else 0.5f
    )
    Box(
        modifier = modifier.fillMaxWidth()
            .clip(shape)
            .background(buttonColor)
            .optionalClickable()
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = CoreColors.secondary.textColor
        )
    }
}
