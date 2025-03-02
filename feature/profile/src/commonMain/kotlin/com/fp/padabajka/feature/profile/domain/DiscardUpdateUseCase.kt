package com.fp.padabajka.feature.profile.domain

import com.fp.padabajka.core.repository.api.DraftProfileRepository

class DiscardUpdateUseCase(private val repository: DraftProfileRepository) {
    suspend operator fun invoke() {
        return repository.discardUpdates()
    }
}
