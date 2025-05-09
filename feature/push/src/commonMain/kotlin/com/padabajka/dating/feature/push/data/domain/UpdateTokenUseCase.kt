package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.PushRepository
import com.padabajka.dating.core.repository.api.model.auth.LoggedIn
import com.padabajka.dating.core.repository.api.model.auth.LoggedOut
import com.padabajka.dating.core.repository.api.model.auth.WaitingForEmailValidation

class UpdateTokenUseCase(
    private val pushRepository: PushRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String) {
        when (authRepository.currentAuthState) {
            LoggedOut -> {}
            is LoggedIn,
            is WaitingForEmailValidation -> pushRepository.saveToken(token)
        }
    }
}
