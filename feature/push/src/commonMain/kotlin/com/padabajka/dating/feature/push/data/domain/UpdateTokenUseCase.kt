package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.repository.api.AuthRepository
import com.padabajka.dating.core.repository.api.model.auth.LoggedIn
import com.padabajka.dating.core.repository.api.model.auth.LoggedOut
import com.padabajka.dating.core.repository.api.model.auth.WaitingForEmailValidation
import com.padabajka.dating.settings.domain.NewAuthMetadataUseCase

class UpdateTokenUseCase(
    private val authRepository: AuthRepository,
    private val newAuthMetadataUseCase: NewAuthMetadataUseCase
) {
    suspend operator fun invoke() {
        when (authRepository.currentAuthState) {
            LoggedOut -> Unit
            is LoggedIn,
            is WaitingForEmailValidation -> {
                newAuthMetadataUseCase.invoke()
            }
        }
    }
}
