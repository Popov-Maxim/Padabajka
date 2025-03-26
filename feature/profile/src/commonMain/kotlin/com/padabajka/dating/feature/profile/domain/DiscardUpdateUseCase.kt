package com.padabajka.dating.feature.profile.domain

import com.padabajka.dating.core.repository.api.DraftProfileRepository

class DiscardUpdateUseCase(private val repository: DraftProfileRepository) {
    suspend operator fun invoke() {
        return repository.discardUpdates()
    }
}
