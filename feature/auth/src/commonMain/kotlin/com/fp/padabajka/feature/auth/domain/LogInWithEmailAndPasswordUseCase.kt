package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.repository.api.AuthRepository

class LogInWithEmailAndPasswordUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String) {

    }
}
