package com.padabajka.dating.core.sync.lifecycle

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillEnterForegroundNotification

class IosAppLifecycle : AppLifecycle {
    override val state: Flow<AppLifecycleState>
        get() = observeAppLifecycle()

    private fun observeAppLifecycle(): Flow<AppLifecycleState> = callbackFlow {
        val notificationCenter = NSNotificationCenter.defaultCenter
        val observers = mutableListOf<Any>()

        observers.add(
            notificationCenter.addObserverForName(
                name = UIApplicationDidEnterBackgroundNotification,
                `object` = null,
                queue = null
            ) { _ ->
                trySend(AppLifecycleState.BACKGROUND)
            }
        )

        observers.add(
            notificationCenter.addObserverForName(
                name = UIApplicationWillEnterForegroundNotification,
                `object` = null,
                queue = null
            ) { _ ->
                trySend(AppLifecycleState.FOREGROUND)
            }
        )

        trySend(AppLifecycleState.FOREGROUND)

        awaitClose {
            observers.forEach { observer ->
                notificationCenter.removeObserver(observer)
            }
            observers.clear()
        }
    }
}
