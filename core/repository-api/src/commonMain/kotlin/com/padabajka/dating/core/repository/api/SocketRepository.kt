package com.padabajka.dating.core.repository.api

import kotlinx.coroutines.flow.Flow

interface SocketRepository {
    val message: Flow<String>
    suspend fun startConnecting()
    suspend fun disconnect()
}
