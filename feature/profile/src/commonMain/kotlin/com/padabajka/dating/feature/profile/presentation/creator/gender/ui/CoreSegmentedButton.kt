package com.padabajka.dating.feature.profile.presentation.creator.gender.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor

@Composable
fun SingleChoiceSegmentedButtonRowScope.CoreSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    text: StaticTextId
) {
    val activeContainerColor = CoreColors.secondary.mainColor
    val activeContentColor = CoreColors.secondary.textColor
    val inactiveContainerColor = Color.Transparent
    val inactiveContentColor = CoreColors.background.textColor

    val animatedActiveContainerColor by animateColorAsState(
        targetValue = if (selected) activeContainerColor else inactiveContainerColor,
    )

    val animatedActiveContentColor by animateColorAsState(
        targetValue = if (selected) activeContentColor else inactiveContentColor,
    )

    val animatedInactiveContainerColor by animateColorAsState(
        targetValue = if (selected.not()) inactiveContainerColor else activeContainerColor,
    )

    val animatedInactiveContentColor by animateColorAsState(
        targetValue = if (selected.not()) inactiveContentColor else activeContentColor,
    )
    val colors = SegmentedButtonDefaults.colors(
        activeContainerColor = animatedActiveContainerColor,
        activeContentColor = animatedActiveContentColor,
        inactiveContainerColor = animatedInactiveContainerColor,
        inactiveContentColor = animatedInactiveContentColor
    )
    SegmentedButton(
        selected = selected,
        onClick = onClick,
        colors = colors,
        border = if (selected) {
            SegmentedButtonDefaults.borderStroke(Color.Transparent, 0.dp)
        } else {
            SegmentedButtonDefaults.borderStroke(Color.Black, 0.5.dp)
        },
        label = {
            Text(
                text = text.translate(),
            )
        },
        icon = {},
        shape = RoundedCornerShape(20.dp),
    )
}
