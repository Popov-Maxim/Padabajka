package com.padabajka.dating.core.sync.lifecycle

import kotlinx.coroutines.flow.Flow

interface AppLifecycle {
    val state: Flow<AppLifecycleState>
}
enum class AppLifecycleState {
    FOREGROUND,
    BACKGROUND
}
