package com.padabajka.dating.core.presentation.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable

@Composable
@NonRestartableComposable
fun EventEffect(event: StateEvent, onConsumed: () -> Unit, action: suspend () -> Unit) {
    LaunchedEffect(event) {
        if (event is StateEvent.Raised) {
            action()
            onConsumed()
        }
    }
}

@Composable
@NonRestartableComposable
fun <T> EventEffect(
    eventWithContent: StateEventWithContent<T>,
    onConsumed: () -> Unit,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(eventWithContent) {
        if (eventWithContent is StateEventWithContent.RaisedWithContent) {
            action(eventWithContent.content)
            onConsumed()
        }
    }
}
