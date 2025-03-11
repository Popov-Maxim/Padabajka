package com.fp.padabajka.feature.auth.domain

import com.fp.padabajka.core.repository.api.AuthRepository

class SendEmailVerificationUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.sendEmailVerification()
    }
}
