package com.padabajka.dating.feature.push.socket.data

import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.feature.push.socket.data.network.KtorSocketApi
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("TooGenericExceptionCaught")
class KtorSocketRepository(
    private val scope: CoroutineScope,
    private val ktorSocketApi: KtorSocketApi,
    private val metadataRepository: MetadataRepository,
) : SocketRepository {

    private var socketSession: DefaultClientWebSocketSession? = null

    private val _messages = MutableSharedFlow<String>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val messages: Flow<String> = _messages.asSharedFlow()

    private val _connectionState = MutableStateFlow(SocketRepository.ConnectionState.DISCONNECTED)
    override val connectionState: Flow<SocketRepository.ConnectionState> =
        _connectionState.asStateFlow()

    override suspend fun connect() {
        if (socketSession != null) {
            log("already connected")
            return
        }

        _connectionState.value = SocketRepository.ConnectionState.CONNECTING
        log("startConnecting")

        try {
            val deviceUid = metadataRepository.getDeviceUid()
            val session = ktorSocketApi.connect(deviceUid.raw)
            socketSession = session
            _connectionState.value = SocketRepository.ConnectionState.CONNECTED
            log("connected")

            scope.launch {
                try {
                    session.incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            _messages.emit(frame.readText())
                        }
                    }
                } finally {
                    disconnectInternal()
                }
            }
        } catch (e: Exception) {
            _connectionState.value = SocketRepository.ConnectionState.DISCONNECTED
            log("connection error: ${e.message}")
        }
    }

    override suspend fun disconnect() {
        disconnectInternal()
    }

    private suspend fun disconnectInternal() {
        socketSession?.close()
        socketSession = null
        _connectionState.value = SocketRepository.ConnectionState.DISCONNECTED
        log("disconnected")
    }

    private companion object {
        private fun log(message: String) {
            println("LOG: websocket $message")
        }
    }
}
