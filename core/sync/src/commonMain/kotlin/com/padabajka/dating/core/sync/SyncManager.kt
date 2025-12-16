package com.padabajka.dating.core.sync

import com.padabajka.dating.core.data.atomic
import com.padabajka.dating.core.data.lockWith
import com.padabajka.dating.core.domain.sync.SyncRemoteDataUseCase
import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.feature.push.data.domain.HandlePushUseCase
import com.padabajka.dating.feature.push.data.domain.model.MessagePush
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
        DISCONNECTED, CONNECTING, SYNCING, ONLINE
    }

    private var state = State.DISCONNECTED
    private val buffer = atomic(mutableListOf<MessagePush>())
    private var reconnectJob: Job? = null

    fun start() {
        observeSocket()
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
                syncRemoteDataUseCase()
                state = State.ONLINE

                buffer.lockWith {
                    forEach { handlePushUseCase(it) }
                    clear()
                }
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

    private fun parse(raw: String): MessagePush =
        MessagePush.Json(Json.decodeFromString(raw))

    private companion object {
        private const val RECONNECT_DELAY = 5_000L
        private fun log(message: String) = println("LOG: SyncManager $message")
    }
}
