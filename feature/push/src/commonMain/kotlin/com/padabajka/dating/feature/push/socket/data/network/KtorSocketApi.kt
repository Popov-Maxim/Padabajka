package com.padabajka.dating.feature.push.socket.data.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.NetworkConstants
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.path

class KtorSocketApi(
    private val ktorClientProvider: KtorClientProvider
) {

    suspend fun connect(deviceUid: String): DefaultClientWebSocketSession {
        val client = ktorClientProvider.client()

        return client.webSocketSession {
            url {
                path(SOCKET_PATH)
                port = NetworkConstants.PORT
            }
            headers.append(DEVICE_UID_KEY, deviceUid)
        }
    }

    private companion object {
        private const val SOCKET_PATH = "connection"
        private const val DEVICE_UID_KEY = "DeviceUid"
    }
}
