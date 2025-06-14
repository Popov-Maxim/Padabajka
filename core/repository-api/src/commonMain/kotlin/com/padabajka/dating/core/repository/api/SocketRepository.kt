package com.padabajka.dating.core.repository.api

import kotlinx.coroutines.flow.Flow

interface SocketRepository {
    suspend fun connectAndGetFlow(): Flow<String>
    suspend fun disconnect()
}
