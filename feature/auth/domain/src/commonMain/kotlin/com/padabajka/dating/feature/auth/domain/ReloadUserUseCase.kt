package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.repository.api.AuthRepository

class ReloadUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.reloadUser()
    }
}
