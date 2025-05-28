package com.padabajka.dating.core.presentation.ui.drawable.icon

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Icon(
    iconData: IconData,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (iconData) {
        is IconData.Painter -> {
            Icon(
                painter = iconData.painter,
                modifier = modifier,
                contentDescription = contentDescription,
                tint = tint
            )
        }

        is IconData.Vector -> {
            Icon(
                imageVector = iconData.vector,
                modifier = modifier,
                contentDescription = contentDescription,
                tint = tint
            )
        }

        IconData.Empty -> {
            Box(modifier = modifier)
        }
    }
}
