package com.padabajka.dating.core.sync

import com.padabajka.dating.core.data.atomic
import com.padabajka.dating.core.data.lockWith
import com.padabajka.dating.core.domain.sync.SyncRemoteDataUseCase
import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.repository.api.exception.BadStatusCodeException
import com.padabajka.dating.core.repository.api.exception.ConnectException
import com.padabajka.dating.core.utils.isDebugBuild
import com.padabajka.dating.feature.push.data.domain.HandlePushUseCase
import com.padabajka.dating.feature.push.data.domain.model.MessagePush
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Suppress("TooGenericExceptionCaught")
class SyncManager(
    private val scope: CoroutineScope,
    private val socketRepository: SocketRepository,
    private val syncRemoteDataUseCase: SyncRemoteDataUseCase,
    private val handlePushUseCase: HandlePushUseCase,
) {

    private enum class State {
        DISCONNECTED, CONNECTING, SYNCING, ONLINE, TURNED_OFF
    }

    private var state = State.DISCONNECTED
    private val buffer = atomic(mutableListOf<MessagePush>())
    private var reconnectJob: Job? = null
    private var observed = false

    fun start() {
        if (observed.not()) {
            observeSocket()
            observed = true
        }
        connect()
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
                        state = State.DISCONNECTED
                        scheduleReconnect()
                    }

                    SocketRepository.ConnectionState.CONNECTING -> {
                        log("socket connecting...")
                        state = State.CONNECTING
                    }

                    SocketRepository.ConnectionState.TURNED_OFF -> {
                        log("socket turn off")
                        state = State.TURNED_OFF
                    }
                }
            }
        }

        scope.launch {
            socketRepository.messages.collect { raw ->
                val push = parse(raw)
                onSocketEvent(push)
            }
        }
    }

    private fun connect() {
        scope.launch {
            try {
                socketRepository.connect()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                scheduleReconnect()
            }
        }
    }

    private fun scheduleReconnect() {
        if (reconnectJob?.isActive == true) return

        reconnectJob = scope.launch {
            delay(RECONNECT_DELAY)
            log("reconnecting...")
            connect()
        }
    }

    private fun onSocketConnected() {
        startSync()
    }

    private fun startSync() {
        state = State.SYNCING

        scope.launch {
            try {
                log("start sync")
                retryUntilSuccess(
                    shouldRetry = { e ->
                        if (e !is ConnectException && e !is BadStatusCodeException) {
                            if (isDebugBuild) {
                                throw e
                            } else {
                                Firebase.crashlytics.recordException(e)
                            }
                        }
                        delay(timeMillis = 5_000)
                        state == State.SYNCING
                    }
                ) {
                    syncRemoteDataUseCase()
                }
                state = State.ONLINE

                buffer.lockWith {
                    forEach { handlePushUseCase(it) }
                    clear()
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (e: Exception) {
                log("sync failed: ${e.message}")
                state = State.DISCONNECTED
                scheduleReconnect()
            }
        }
    }

    private suspend fun onSocketEvent(event: MessagePush) {
        when (state) {
            State.SYNCING -> buffer.lockWith { add(event) }
            State.ONLINE -> handlePushUseCase(event)
            else -> Unit
        }
    }

    private suspend fun retryUntilSuccess(
        shouldRetry: suspend (Throwable) -> Boolean = { true },
        block: suspend () -> Unit
    ) {
        while (true) {
            try {
                return block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                if (!shouldRetry(e)) break
            }
        }
    }

    private fun parse(raw: String): MessagePush =
        MessagePush.Json(Json.decodeFromString(raw))

    private companion object {
        private const val RECONNECT_DELAY = 5_000L
        private fun log(message: String) = println("LOG: SyncManager $message")
    }
}
