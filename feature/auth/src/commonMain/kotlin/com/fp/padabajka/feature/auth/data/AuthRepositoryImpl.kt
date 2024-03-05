package com.fp.padabajka.feature.auth.data

import com.fp.padabajka.core.repository.api.AuthRepository
import com.fp.padabajka.core.repository.api.model.auth.AuthState
import com.fp.padabajka.core.repository.api.model.auth.Credentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl(
    private val localDataSource: LocalAuthDataSource,
    private val remoteAuthDataSource: RemoteAuthDataSource
) : AuthRepository {

    private val _authState = MutableStateFlow<AuthState>()
    override val authState = _authState.asStateFlow()

    override suspend fun login(credentials: Credentials) {
        TODO("Not yet implemented")
    }

    override suspend fun register(credentials: Credentials) {
        TODO("Not yet implemented")
    }
}
