package com.fp.padabajka.core.repository.api

import com.fp.padabajka.core.repository.api.model.auth.AuthState
import com.fp.padabajka.core.repository.api.model.auth.Credentials
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authState: Flow<AuthState>
    suspend fun login(credentials: Credentials)
    suspend fun login(email: String, password: String)
    suspend fun register(credentials: Credentials)
}
