package com.padabajka.dating.core.sync

import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.sync.lifecycle.AppLifecycle
import com.padabajka.dating.core.sync.lifecycle.AppLifecycleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class SyncSessionObserver(
    private val scope: CoroutineScope,
    private val lifecycle: AppLifecycle,
    private val syncManagerFactory: SyncManagerFactory,
) {

    private val sessionMutex = Mutex()
    private val managerFlow = MutableStateFlow<SyncManager?>(null)
    private var job: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    val stateFlow = managerFlow
        .flatMapLatest { manager ->
            manager?.stateFlow ?: flowOf(SyncManager.State.TURNED_OFF)
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = SyncManager.State.TURNED_OFF,
        )

    private val lifecycleState by lazy { lifecycle.state }

    /** Starts a new user session after fully closing the previous one, even for the same user. */
    suspend fun start(userId: UserId): Unit = sessionMutex.withLock {
        closeSession()
        currentCoroutineContext().ensureActive()

        val manager = syncManagerFactory.create(userId)
        managerFlow.value = manager

        job = scope.launch(Dispatchers.Main) {
            lifecycleState
                .distinctUntilChanged()
                .collect { state ->
                    when (state) {
                        AppLifecycleState.FOREGROUND -> manager.start()
                        AppLifecycleState.BACKGROUND -> manager.stop()
                    }
                }
        }
    }

    /** Ends the user session; background transitions only pause the current manager. */
    suspend fun stop(): Unit = withContext(NonCancellable) {
        sessionMutex.withLock { closeSession() }
    }

    private suspend fun closeSession(): Unit = withContext(NonCancellable) {
        job?.cancelAndJoin()
        job = null

        try {
            managerFlow.value?.close()
        } finally {
            managerFlow.value = null
        }
    }
}
