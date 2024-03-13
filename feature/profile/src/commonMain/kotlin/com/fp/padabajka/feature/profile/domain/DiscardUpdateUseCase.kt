package com.fp.padabajka.feature.profile.domain

import com.fp.padabajka.core.repository.api.EditProfileRepository

class DiscardUpdateUseCase(private val repository: EditProfileRepository) {
    suspend operator fun invoke() {
        return repository.discardUpdates()
    }
}
