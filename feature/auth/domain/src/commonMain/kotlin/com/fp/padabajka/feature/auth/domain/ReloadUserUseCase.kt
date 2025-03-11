package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.repository.api.AuthRepository

class ReloadUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.reloadUser()
    }
}
