package com.padabajka.dating.core.repository.api

import kotlinx.coroutines.flow.Flow

interface SocketRepository {
    val messages: Flow<String>
    val connectionState: Flow<ConnectionState>

    suspend fun connect()
    suspend fun disconnect()

    enum class ConnectionState {
        TURNED_OFF, DISCONNECTED, CONNECTING, CONNECTED
    }
}
