package com.padabajka.dating.feature.push.socket.data

import com.padabajka.dating.core.repository.api.SocketRepository
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.feature.push.socket.data.network.KtorSocketApi
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class KtorSocketRepository(
    private val ktorSocketApi: KtorSocketApi,
    private val metadataRepository: MetadataRepository
) : SocketRepository {

    private var socketSession: DefaultClientWebSocketSession? = null

    override suspend fun connectAndGetFlow(): Flow<String> {
        disconnect()

        val deviceUid = metadataRepository.getDeviceUid()
        return ktorSocketApi.connect(deviceUid.raw).apply {
            socketSession = this
        }.incoming.receiveAsFlow()
            .filterIsInstance<Frame.Text>()
            .map { it.readText() }
    }

    override suspend fun disconnect() {
        socketSession?.close()
        socketSession = null
    }
}
