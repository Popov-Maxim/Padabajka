package com.fp.padabajka.core.presentation.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun EventEffect(event: StateEvent, onConsumed: () -> Unit, action: suspend () -> Unit) {
    LaunchedEffect(event) {
        if (event is StateEvent.Raised) {
            action()
            onConsumed()
        }
    }
}

@Composable
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
