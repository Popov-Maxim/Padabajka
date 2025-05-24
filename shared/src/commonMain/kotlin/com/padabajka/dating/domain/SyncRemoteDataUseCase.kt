package com.padabajka.dating.domain

import com.padabajka.dating.core.repository.api.MatchRepository

class SyncRemoteDataUseCase(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke() {
        matchRepository.sync()
    }
}
