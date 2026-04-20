package com.padabajka.dating.feature.messenger.presentation.chat.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.padabajka.dating.core.presentation.ui.CoreColors
import com.padabajka.dating.core.presentation.ui.mainColor
import com.padabajka.dating.core.presentation.ui.textColor

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = (-10).dp),
        ) {
            Box(
                modifier = Modifier
//                        .shadow(4.dp)
                    .zIndex(1f)
                    .background(CoreColors.Chat.message.mainColor, CircleShape)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "$count",
                    color = CoreColors.Chat.message.textColor
                )
            }
            Box(
                modifier = Modifier
                    .zIndex(0f)
                    .shadow(4.dp, CircleShape)
                    .size(52.dp)
                    .background(Color.White, CircleShape)
                    .clickable(onClick = onClick)
                    .padding(10.dp), // TODO(P1): need 15.dp
            ) {
                Icon(
                    modifier = Modifier.matchParentSize(),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Go to unread message"
                )
            }
        }
    }
}
