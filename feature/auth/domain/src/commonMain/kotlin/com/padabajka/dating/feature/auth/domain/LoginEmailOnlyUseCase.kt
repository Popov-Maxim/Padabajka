package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.repository.api.AuthRepository

class LoginEmailOnlyUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String) {
        repository.loginWithoutPassword(email)
    }
}
