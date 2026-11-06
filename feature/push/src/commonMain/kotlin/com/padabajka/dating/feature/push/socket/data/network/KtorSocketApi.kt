package com.padabajka.dating.feature.push.socket.data.network

import com.padabajka.dating.core.networking.KtorClientProvider
import com.padabajka.dating.core.networking.NetworkConstants
import com.padabajka.dating.core.networking.socketProtocol
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

class KtorSocketApi(
    private val ktorClientProvider: KtorClientProvider,
    private val appSettings: AppSettingsRepository
) {

    /**
     * SOCKET GET /connection
     */
    suspend fun connect(deviceUid: String): DefaultClientWebSocketSession {
        val client = ktorClientProvider.client()
        var requestJob: Job? = null

        try {
            return client.webSocketSession {
                requestJob = executionContext
                url {
                    val debugHost = appSettings.debugAppSettingsValue.host
                    protocol = debugHost.socketProtocol() ?: URLProtocol.WSS
                    path(SOCKET_PATH)
                    port = NetworkConstants.PORT
                }
                headers.append(DEVICE_UID_KEY, deviceUid)
            }
        } catch (exception: CancellationException) {
            // Ktor opens the session in the client scope; cancelling its await is not enough.
            requestJob?.cancel(exception)
            throw exception
        }
    }

    private companion object {
        private const val SOCKET_PATH = "connection"
        private const val DEVICE_UID_KEY = "DeviceUid"
    }
}
