package com.fp.padabajka.feature.messenger.presentation.element

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ArrowDownCounterButton(
    modifier: Modifier,
    count: Int,
    visible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible
    ) {
        Button(
            modifier = Modifier.background(Color.Yellow).size(56.dp),
            onClick = onClick
        ) {
            Box {
                Icon(
                    modifier = Modifier.padding(16.dp),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Go to unread message"
                )
                Text(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    text = "$count"
                )
            }
        }
    }
}
