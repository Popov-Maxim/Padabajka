package com.padabajka.dating.feature.messenger.presentation.chat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.RemoveParentMessageEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.item.ParentMessageItem

@Composable
fun ParentMessageField(
    modifier: Modifier = Modifier,
    parentMessage: ParentMessageItem,
    onEvent: (MessengerEvent) -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        CommonParentMessage(
            parentMessage = parentMessage,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                onEvent(RemoveParentMessageEvent)
            }
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.Close),
                contentDescription = "Remove parent message"
            )
        }
    }
}
