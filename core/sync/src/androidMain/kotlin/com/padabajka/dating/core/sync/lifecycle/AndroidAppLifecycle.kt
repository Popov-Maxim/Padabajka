package com.padabajka.dating.core.sync.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidAppLifecycle : AppLifecycle {
    override val state: Flow<AppLifecycleState>
        get() = observeAppLifecycle()

    private fun observeAppLifecycle(): Flow<AppLifecycleState> = callbackFlow {
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                trySend(AppLifecycleState.FOREGROUND)
            }

            override fun onStop(owner: LifecycleOwner) {
                trySend(AppLifecycleState.BACKGROUND)
            }
        }

        val lifecycle = ProcessLifecycleOwner.get().lifecycle
        lifecycle.addObserver(observer)

        val initialState = if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            AppLifecycleState.FOREGROUND
        } else {
            AppLifecycleState.BACKGROUND
        }
        trySend(initialState)

        awaitClose {
            lifecycle.removeObserver(observer)
        }
    }
}
