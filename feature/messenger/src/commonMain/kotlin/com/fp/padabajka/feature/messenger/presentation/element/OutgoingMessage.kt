package com.fp.padabajka.feature.messenger.presentation.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fp.padabajka.core.presentation.hourMinutes
import com.fp.padabajka.feature.messenger.presentation.model.MessengerEvent
import com.fp.padabajka.feature.messenger.presentation.model.item.OutgoingMessageItem

@Suppress("UnusedParameter")
@Composable
fun OutgoingMessage(
    message: OutgoingMessageItem,
    onEvent: (MessengerEvent) -> Unit
) {
    Box(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        Box(Modifier.align(Alignment.CenterEnd).background(Color.Green).padding(8.dp)) {
            Column {
                Text(message.sentTime.hourMinutes)
                Text(message.content)
            }
        }
    }
}
