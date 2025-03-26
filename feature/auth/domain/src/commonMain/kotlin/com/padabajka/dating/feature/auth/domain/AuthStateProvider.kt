package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.model.auth.AuthState
import kotlinx.coroutines.flow.Flow

class AuthStateProvider(private val authRepository: AuthRepository) {
    val authState: Flow<AuthState>
        get() = authRepository.authState
    val currentAuthState: AuthState
        get() = authRepository.currentAuthState
}
