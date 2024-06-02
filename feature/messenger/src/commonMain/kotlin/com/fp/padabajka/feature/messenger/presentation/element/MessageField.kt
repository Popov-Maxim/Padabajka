package com.fp.padabajka.feature.messenger.presentation.element

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.fp.padabajka.feature.messenger.presentation.model.MessengerEvent
import com.fp.padabajka.feature.messenger.presentation.model.NextMessageTextUpdateEvent
import com.fp.padabajka.feature.messenger.presentation.model.SendMessageClickEvent

// TODO: implement proper ui
@Composable
fun MessageField(
    modifier: Modifier = Modifier,
    text: () -> String,
    followNewItems: MutableState<Boolean>,
    onEvent: (MessengerEvent) -> Unit
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = modifier.weight(1F),
            value = text(),
            onValueChange = { onEvent(NextMessageTextUpdateEvent(it)) }
        )
        Button(
            onClick = {
                followNewItems.value = true
                onEvent(SendMessageClickEvent)
            }
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Filled.ArrowForward),
                contentDescription = "Send message"
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
    }
}
