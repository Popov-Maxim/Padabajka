package com.padabajka.dating.core.presentation.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable

@Composable
@NonRestartableComposable
fun NavigationEventEffect(
    event: StateEvent,
    onConsumed: () -> Unit,
    action: suspend () -> Unit
) {
    LaunchedEffect(event) {
        if (event is StateEvent.Raised) {
            onConsumed()
            action()
        }
    }
}

@Composable
@NonRestartableComposable
fun <T> NavigationEventEffect(
    event: StateEventWithContent<T>,
    onConsumed: () -> Unit,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(event) {
        if (event is StateEventWithContent.RaisedWithContent) {
            onConsumed()
            action(event.content)
        }
    }
}
