package com.padabajka.dating.feature.messenger.presentation.chat.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.translate
import com.padabajka.dating.feature.messenger.presentation.chat.model.MessengerEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NextMessageFieldLostFocusEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.NextMessageTextUpdateEvent
import com.padabajka.dating.feature.messenger.presentation.chat.model.SendMessageClickEvent

// TODO: implement proper ui
@Composable
fun MessageField(
    modifier: Modifier = Modifier,
    text: String,
    followNewItems: MutableState<Boolean>,
    onEvent: (MessengerEvent) -> Unit
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = modifier
                .weight(1F)
                .onFocusChanged {
                    if (it.hasFocus.not()) {
                        onEvent(NextMessageFieldLostFocusEvent)
                    }
                },
            value = text,
            onValueChange = { onEvent(NextMessageTextUpdateEvent(it)) },
            colors = TextFieldDefaults.transparentColors(),
            placeholder = { Text(StaticTextId.UiId.EnterMessage.translate()) },
            prefix = { Spacer(Modifier.width(30.dp)) }
        )
        IconButton(
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, end = 10.dp),
            onClick = {
                followNewItems.value = true
                onEvent(SendMessageClickEvent(text))
            }
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.AutoMirrored.Filled.ArrowForward),
                contentDescription = "Send message"
            )
        }
    }
}

@Composable
private fun TextFieldDefaults.transparentColors(
    textColor: Color = Color.Unspecified,
    cursorColor: Color = Color.Unspecified,
    labelColor: Color = Color.Unspecified,
): TextFieldColors {
    return this.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,

        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,

        cursorColor = cursorColor,

        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        disabledTextColor = textColor,

        focusedLabelColor = labelColor,
        unfocusedLabelColor = labelColor,
        disabledLabelColor = labelColor,
    )
}
