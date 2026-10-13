package com.padabajka.dating.core.sync

import com.padabajka.dating.core.data.atomic
import com.padabajka.dating.core.data.lockWith
import com.padabajka.dating.core.domain.sync.SyncRemoteDataUseCase
import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.repository.api.exception.BadStatusCodeException
import com.padabajka.dating.core.repository.api.exception.ConnectException
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.core.utils.isDebugBuild
import com.padabajka.dating.feature.push.data.domain.HandlePushUseCase
import com.padabajka.dating.feature.push.data.domain.model.MessagePush
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@Suppress("TooGenericExceptionCaught", "PrintStackTrace")
class SyncManager internal constructor(
    val userId: UserId,
    private val scope: CoroutineScope,
    private val socketRepository: SocketRepository,
    private val syncRemoteDataUseCase: SyncRemoteDataUseCase,
    private val handlePushUseCase: HandlePushUseCase,
    private val authRepository: AuthRepository,
) {

    enum class State {
        DISCONNECTED, CONNECTING, SYNCING, ONLINE, TURNED_OFF
    }

    private enum class SyncRequest {
        REGULAR, RECOVERY
    }

    private val _stateFlow = MutableStateFlow(State.TURNED_OFF)
    val stateFlow = _stateFlow.asStateFlow()

    private var state = State.TURNED_OFF
        set(value) {
            field = value
            _stateFlow.value = value
        }
    private val buffer = atomic(mutableListOf<MessagePush>())
    private var reconnectJob: Job? = null
    private var connectJob: Job? = null
    private var syncJob: Job? = null
    private var pendingSyncRequest: SyncRequest? = null
    private var activeSyncRequest: SyncRequest? = null
    private val eventMutex = Mutex()
    private val lifecycleMutex = Mutex()
    private var observed = false
    private var closed = false

    suspend fun start(): Unit = lifecycleMutex.withLock {
        if (closed || isCurrentUser().not()) return@withLock

        eventMutex.withLock {
            if (observed.not()) {
                observeSocket()
                observed = true
            }
            if (state == State.TURNED_OFF) {
                state = State.DISCONNECTED
            }
        }
        connect()
    }

    /** Pauses sync without ending the user session. */
    suspend fun stop(): Unit = withContext(NonCancellable) {
        lifecycleMutex.withLock { stopInternal() }
    }

    /** Permanently ends this session and waits for all of its producers and collectors. */
    suspend fun close(): Unit = withContext(NonCancellable) {
        lifecycleMutex.withLock {
            if (closed) return@withLock

            closed = true
            try {
                stopInternal()
            } finally {
                scope.coroutineContext[Job]?.cancelAndJoin()
            }
        }
    }

    private suspend fun stopInternal() {
        val jobs = eventMutex.withLock {
            state = State.TURNED_OFF
            pendingSyncRequest = null
            buffer.lockWith { clear() }
            listOfNotNull(connectJob, reconnectJob, syncJob)
        }

        jobs.forEach { it.cancel() }
        jobs.forEach { it.join() }
        socketRepository.disconnect()

        eventMutex.withLock {
            buffer.lockWith { clear() }
            connectJob = null
            reconnectJob = null
            syncJob = null
            activeSyncRequest = null
        }
    }

    private fun observeSocket() {
        scope.launch {
            socketRepository.connectionState.collect { connection ->
                when (connection) {
                    SocketRepository.ConnectionState.CONNECTED -> {
                        log("socket connected")
                        onSocketConnected()
                    }

                    SocketRepository.ConnectionState.DISCONNECTED -> {
                        log("socket disconnected")
                        onSocketDisconnected()
                    }

                    SocketRepository.ConnectionState.CONNECTING -> {
                        log("socket connecting...")
                        eventMutex.withLock {
                            if (state != State.TURNED_OFF) {
                                log("state socket connecting...")
                                state = State.CONNECTING
                            }
                        }
                    }

                    SocketRepository.ConnectionState.TURNED_OFF -> {
                        // Only stop() may turn off the manager; the socket's initial or
                        // delayed TURNED_OFF must not override start() or queued sync work.
                        log("socket TURNED_OFF ignored; manager state=${stateFlow.value}")
                    }
                }
            }
        }

        scope.launch {
            socketRepository.messages.collect { raw ->
                handleSocketEvent(raw)
            }
        }
    }

    private suspend fun connect(): Unit = eventMutex.withLock {
        if (state == State.TURNED_OFF || isCurrentUser().not()) return@withLock
        if (connectJob?.isActive == true) return@withLock

        connectJob = scope.launch {
            try {
                val shouldConnect = eventMutex.withLock {
                    state != State.TURNED_OFF && isCurrentUser()
                }
                if (shouldConnect.not()) return@launch

                socketRepository.connect()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Throwable) {
                scheduleReconnect()
            }
        }
    }

    private suspend fun scheduleReconnect(): Unit = eventMutex.withLock {
        if (state == State.TURNED_OFF || isCurrentUser().not()) return@withLock
        if (reconnectJob?.isActive == true) return@withLock

        reconnectJob = scope.launch {
            delay(RECONNECT_DELAY)
            val shouldConnect = eventMutex.withLock {
                state == State.DISCONNECTED && isCurrentUser()
            }
            if (shouldConnect.not()) return@launch

            log("reconnecting...")
            connect()
        }
    }

    private suspend fun onSocketConnected() {
        eventMutex.withLock {
            if (state != State.TURNED_OFF && isCurrentUser()) {
                requestSyncLocked(SyncRequest.REGULAR)
            }
        }
    }

    private suspend fun onSocketDisconnected() {
        val shouldReconnect = eventMutex.withLock {
            if (state == State.TURNED_OFF) {
                false
            } else {
                state = State.DISCONNECTED
                pendingSyncRequest = null
                true
            }
        }
        if (shouldReconnect) {
            scheduleReconnect()
        }
    }

    private fun requestSyncLocked(request: SyncRequest) {
        if (isCurrentUser().not()) return
        if (request == SyncRequest.RECOVERY && activeSyncRequest == SyncRequest.RECOVERY) return

        pendingSyncRequest = when {
            request == SyncRequest.REGULAR -> SyncRequest.REGULAR
            pendingSyncRequest == SyncRequest.REGULAR -> SyncRequest.REGULAR
            else -> SyncRequest.RECOVERY
        }
        state = State.SYNCING
        if (syncJob?.isActive != true) {
            syncJob = scope.launch { runSyncLoop() }
        }
    }

    private suspend fun requestRecoverySync() {
        eventMutex.withLock {
            if (state == State.ONLINE || state == State.SYNCING) {
                requestSyncLocked(SyncRequest.RECOVERY)
            }
        }
    }

    private suspend fun runSyncLoop() {
        val currentJob = currentCoroutineContext()[Job] ?: return
        try {
            var request = takeSyncRequest()
            while (request != null) {
                request = if (syncOnce(request)) takeSyncRequest() else null
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (e: Throwable) {
            log("sync failed: ${e.message}")
            if (isDebugBuild) {
                e.printStackTrace()
            }
            val shouldReconnect = eventMutex.withLock {
                if (state == State.TURNED_OFF) {
                    false
                } else {
                    state = State.DISCONNECTED
                    pendingSyncRequest = null
                    true
                }
            }
            if (shouldReconnect) {
                scheduleReconnect()
            }
            handleException(e)
        } finally {
            withContext(NonCancellable) { finishSyncLoop(currentJob) }
        }
    }

    private suspend fun takeSyncRequest(): SyncRequest? = eventMutex.withLock {
        if (state == State.TURNED_OFF || state == State.DISCONNECTED || isCurrentUser().not()) {
            pendingSyncRequest = null
            null
        } else {
            val request = pendingSyncRequest ?: return@withLock null
            pendingSyncRequest = null
            activeSyncRequest = request
            state = State.SYNCING
            request
        }
    }

    private suspend fun syncOnce(request: SyncRequest): Boolean {
        log("start sync")
        val synced = retryUntilSuccess(
            shouldRetry = { e ->
                log("sync retry failed: ${e.message}")
                if (isDebugBuild) {
                    e.printStackTrace()
                }
                handleException(e)

                delay(timeMillis = 5_000)
                eventMutex.withLock { state == State.SYNCING && isCurrentUser() }
            }
        ) {
            ensureCurrentUser()
            syncRemoteDataUseCase()
            ensureCurrentUser()
        }
        if (synced.not()) return false

        return eventMutex.withLock {
            if (state != State.SYNCING) return@withLock false

            var recoveryRequired = false
            buffer.lockWith {
                forEach { event ->
                    recoveryRequired = handlePushSafely(event).not() || recoveryRequired
                }
                clear()
            }

            if (recoveryRequired && request != SyncRequest.RECOVERY) {
                requestSyncLocked(SyncRequest.RECOVERY)
            }
            activeSyncRequest = null
            state = if (pendingSyncRequest != null) State.SYNCING else State.ONLINE
            true
        }
    }

    private suspend fun finishSyncLoop(currentJob: Job) {
        eventMutex.withLock {
            if (syncJob !== currentJob) return@withLock

            syncJob = null
            activeSyncRequest = null
            if (pendingSyncRequest == null) return@withLock

            if (
                state != State.TURNED_OFF &&
                state != State.DISCONNECTED &&
                isCurrentUser()
            ) {
                syncJob = scope.launch { runSyncLoop() }
            }
        }
    }

    private fun handleException(e: Throwable) {
        if (e !is ConnectException && e !is BadStatusCodeException) {
            if (isDebugBuild) {
                throw e
            } else {
                Firebase.crashlytics.recordException(e)
            }
        }
    }

    private suspend fun handleSocketEvent(raw: String) {
        try {
            val push = parse(raw)
            onSocketEvent(push)
        } catch (exception: CancellationException) {
            throw exception
        } catch (e: Throwable) {
            reportSocketEventException(e)
            requestRecoverySync()
        }
    }

    private suspend fun onSocketEvent(event: MessagePush) {
        eventMutex.withLock {
            if (isCurrentUser().not()) return@withLock
            when (state) {
                State.SYNCING -> {
                    buffer.lockWith { add(event) }
                }
                State.ONLINE -> {
                    if (handlePushSafely(event).not()) {
                        requestSyncLocked(SyncRequest.RECOVERY)
                    }
                }
                else -> Unit
            }
        }
    }

    private suspend fun handlePushSafely(event: MessagePush): Boolean {
        return try {
            handlePushUseCase(event, userId)
            true
        } catch (exception: CancellationException) {
            throw exception
        } catch (e: Throwable) {
            reportSocketEventException(e)
            false
        }
    }

    private fun reportSocketEventException(e: Throwable) {
        log("socket event failed: ${e.message}")
        if (isDebugBuild) {
            e.printStackTrace()
        } else {
            Firebase.crashlytics.recordException(e)
        }
    }

    private suspend fun retryUntilSuccess(
        shouldRetry: suspend (Throwable) -> Boolean = { true },
        block: suspend () -> Unit
    ): Boolean {
        while (true) {
            try {
                block()
                return true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                if (!shouldRetry(e)) return false
            }
        }
    }

    private fun parse(raw: String): MessagePush =
        MessagePush.Json(Json.decodeFromString(raw))

    private fun isCurrentUser(): Boolean =
        authRepository.currentAuthState.userIdOrNull() == userId

    private fun ensureCurrentUser() {
        if (isCurrentUser().not()) throw CancellationException("Sync user session has changed")
    }

    private companion object {
        private const val RECONNECT_DELAY = 5_000L
        private fun log(message: String) = println("LOG: SyncManager $message")
    }
}
