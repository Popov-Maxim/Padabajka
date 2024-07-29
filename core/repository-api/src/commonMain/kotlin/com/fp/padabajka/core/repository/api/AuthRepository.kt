package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.auth.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authState: Flow<AuthState>
    val currentAuthState: AuthState
    suspend fun authToken(): String?
    suspend fun login(token: String)
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun logout()
}
