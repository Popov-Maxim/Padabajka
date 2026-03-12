package com.padabajka.dating.core.presentation

import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun <T : Any> Value<T>.asFlow(): Flow<T> = callbackFlow {
    // Emit initial value
    trySend(this@asFlow.value)

    // Subscribe to changes
    val observer: (T) -> Unit = { newState: T ->
        trySend(newState)
    }
    val disposable = observe(observer)

    // Unsubscribe when flow is cancelled
    awaitClose { disposable.cancel() }
}
