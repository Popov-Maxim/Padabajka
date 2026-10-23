package com.padabajka.dating.feature.push.socket.data

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.core.repository.api.model.auth.UserId
import com.padabajka.dating.core.repository.api.model.auth.userIdOrNull
import com.padabajka.dating.feature.push.socket.data.network.KtorSocketApi
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@Suppress("TooGenericExceptionCaught")
class KtorSocketRepository(
    private val userId: UserId,
    private val scope: CoroutineScope,
    private val ktorSocketApi: KtorSocketApi,
    private val metadataRepository: MetadataRepository,
    private val authRepository: AuthRepository,
) : SocketRepository {

    private val connectionMutex = Mutex()
    private var socketSession: DefaultClientWebSocketSession? = null
    private var readerJob: Job? = null

    private val _messages = MutableSharedFlow<String>(
        replay = 256,
    )
    override val messages: Flow<String> = _messages.asSharedFlow()

    private val _connectionState = MutableStateFlow(SocketRepository.ConnectionState.TURNED_OFF)
    override val connectionState: Flow<SocketRepository.ConnectionState> =
        _connectionState.asStateFlow()

    override suspend fun connect(): Unit = connectionMutex.withLock {
        if (socketSession != null) {
            log("already connected")
            return@withLock
        }

        _connectionState.value = SocketRepository.ConnectionState.CONNECTING
        log("startConnecting")

        try {
            ensureCurrentUser()
            val deviceUid = metadataRepository.getDeviceUid()
            ensureCurrentUser()
            val session = ktorSocketApi.connect(deviceUid.raw)
            try {
                currentCoroutineContext().ensureActive()
                ensureCurrentUser()
            } catch (exception: Throwable) {
                session.cancel()
                throw exception
            }

            socketSession = session
            val reader = scope.launch(start = CoroutineStart.LAZY) { readSocket(session) }
            readerJob = reader
            _connectionState.value = SocketRepository.ConnectionState.CONNECTED
            log("connected")
            reader.start()
        } catch (exception: CancellationException) {
            _connectionState.value = SocketRepository.ConnectionState.DISCONNECTED
            throw exception
        } catch (e: Throwable) {
            _connectionState.value = SocketRepository.ConnectionState.DISCONNECTED
            log("connection error: ${e.message}")
        }
    }

    override suspend fun disconnect(): Unit = withContext(NonCancellable) {
        val (session, reader) = connectionMutex.withLock {
            val connection = socketSession to readerJob
            socketSession = null
            readerJob = null
            _connectionState.value = SocketRepository.ConnectionState.TURNED_OFF
            connection
        }

        try {
            session?.close()
        } catch (exception: Throwable) {
            log("socket close failed: ${exception.message}")
        } finally {
            session?.cancel()
            reader?.cancelAndJoin()
        }
        log("disconnected")
    }

    private suspend fun readSocket(session: DefaultClientWebSocketSession) {
        try {
            session.incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    _messages.emit(frame.readText())
                }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            log("socket read failed: ${exception.message}")
        } finally {
            withContext(NonCancellable) {
                session.cancel()
                connectionMutex.withLock {
                    // A reader can only detach its own connection, never a newer one.
                    if (socketSession === session) {
                        socketSession = null
                        readerJob = null
                        _connectionState.value = SocketRepository.ConnectionState.DISCONNECTED
                    }
                }
            }
        }
    }

    private fun ensureCurrentUser() {
        if (authRepository.currentAuthState.userIdOrNull() != userId) {
            throw CancellationException("Socket user session has changed")
        }
    }

    private companion object {
        private fun log(message: String) {
            println("LOG: websocket $message")
        }
    }
}
