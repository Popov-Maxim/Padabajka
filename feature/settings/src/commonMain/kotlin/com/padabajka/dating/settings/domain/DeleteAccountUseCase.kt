package com.padabajka.dating.settings.domain

import com.padabajka.dating.core.repository.api.AccountRepository
import com.padabajka.dating.core.repository.api.AuthRepository

class DeleteAccountUseCase(
    private val accountRepository: AccountRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        accountRepository.delete()
        authRepository.logout()
    }
}
