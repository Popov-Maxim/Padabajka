package com.padabajka.dating.feature.auth.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.CoreCallToActionButton
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.IconData
import com.padabajka.dating.core.presentation.ui.textColor

@Composable
fun AuthMethodButton(
    text: String,
    icon: IconData? = null,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        modifier = Modifier.border(1.dp, Color.Black, shape).fillMaxWidth().clip(shape)
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.CenterHorizontally
        ),
    ) {
        if (icon != null) {
            Icon(
                iconData = icon,
                tint = CoreColors.background.textColor,
                modifier = Modifier.size(20.dp)
            )
        }
        val padding = if (icon != null) 10.dp else 0.dp // TODO: need?
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = padding),
        )
    }
}

@Composable
fun CallToActionButton(
    text: String,
    onClick: () -> Unit
) {
    CoreCallToActionButton(
        text = text,
        onClick = onClick
    )
}
