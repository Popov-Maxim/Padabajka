package com.padabajka.dating.feature.auth.domain

import com.padabajka.dating.core.repository.api.AuthRepository

class SendEmailVerificationUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() {
        authRepository.sendEmailVerification()
    }
}
