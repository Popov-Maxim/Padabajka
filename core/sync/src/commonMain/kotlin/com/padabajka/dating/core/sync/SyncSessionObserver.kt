package com.padabajka.dating.core.sync

import com.padabajka.dating.core.sync.lifecycle.AppLifecycle
import com.padabajka.dating.core.sync.lifecycle.AppLifecycleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SyncSessionObserver(
    private val scope: CoroutineScope,
    private val lifecycle: AppLifecycle,
    private val syncManager: SyncManager,
) {

    private var job: Job? = null

    private val lifecycleState by lazy { lifecycle.state }

    fun start() {
        if (job != null) return

        job = scope.launch(Dispatchers.Main) {
            lifecycleState
                .distinctUntilChanged()
                .collect { state ->
                    when (state) {
                        AppLifecycleState.FOREGROUND -> syncManager.start()
                        AppLifecycleState.BACKGROUND -> syncManager.stop()
                    }
                }
        }
    }

    suspend fun stop() {
        job?.cancel()
        job = null

        syncManager.stop()
    }
}
