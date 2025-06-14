package com.padabajka.dating.feature.push.socket.data

import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.feature.push.socket.data.network.KtorSocketApi
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class KtorSocketRepository(
    private val ktorSocketApi: KtorSocketApi,
    private val metadataRepository: MetadataRepository
) : SocketRepository {

    private var socketSession: DefaultClientWebSocketSession? = null
    private var active = false
    private var _messages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    override val message: Flow<String> = _messages.asSharedFlow()

    override suspend fun startConnecting() {
        log("startConnecting")
        if (active) return log("already active")

        active = true
        while (active) {
            runCatching {
                connect()
            }
            log("disconnected")
            delay(DELAY_BEFORE_RECONNECTING)
        }
    }

    private suspend fun connect() {
        val deviceUid = metadataRepository.getDeviceUid()
        ktorSocketApi.connect(deviceUid.raw).apply {
            socketSession = this
            log("connected")
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    _messages.emit(frame.readText())
                }
            }
        }
    }

    override suspend fun disconnect() {
        socketSession?.close()
        socketSession = null
        active = false
    }

    private companion object {
        private const val DELAY_BEFORE_RECONNECTING = 5_000L

        private fun log(message: String) {
            println("LOG: websocket $message")
        }
    }
}
