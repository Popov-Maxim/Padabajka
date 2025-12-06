package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.padabajka.dating.core.presentation.ui.drawable.icon.Icon
import com.padabajka.dating.core.presentation.ui.drawable.icon.IconData

@Composable
fun CoreChipItem(
    text: String,
    icon: IconData? = null,
    modifier: Modifier = Modifier,
    colorGroup: ColorGroup = CoreColors.Chips.selected,
    containerColor: Color = colorGroup.mainColor,
    labelColor: Color = colorGroup.textColor,
    leadingIconContentColor: Color = colorGroup.textColor,
    border: BorderStroke? = null,
) {
    CoreChip(
        label = {
            Text(
                text = text,
                fontSize = 16.sp,
                color = labelColor
            )
        },
        modifier = modifier,
        leadingIcon = { LeadingIcon(icon, leadingIconContentColor) },
        shape = RoundedCornerShape(20.dp),
        containerColor = containerColor,
        border = border,
    )
}

@Composable
private fun LeadingIcon(icon: IconData?, color: Color) {
    if (icon != null) {
        Icon(
            iconData = icon,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun CoreChip(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape,
    containerColor: Color,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    border: BorderStroke? = null,
) {
//    val startPadding = if (leadingIcon == null) 12.dp else 6.dp
//    val endPadding = if (trailingIcon == null) 12.dp else 6.dp
    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .then(
                if (border != null) Modifier.border(border, shape) else Modifier
            )
            .padding(vertical = 6.dp, horizontal = 12.dp)
//            .padding(start = startPadding, end = endPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            }

            label()

            if (trailingIcon != null) {
                trailingIcon()
            }
        }
    }
}
