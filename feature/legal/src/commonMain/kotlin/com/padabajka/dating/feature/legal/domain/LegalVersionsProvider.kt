package com.padabajka.dating.feature.legal.domain

import com.padabajka.dating.core.repository.api.metadata.LegalRepository
import com.padabajka.dating.core.repository.api.model.legal.LegalVersions

class LegalVersionsProvider(
    private val legalRepository: LegalRepository
) {
    suspend fun get(): LegalVersions {
        return legalRepository.actualVersions()
    }

    companion object {
        val default = LegalVersions(
            privacy = "legal_test_2026_01_01",
            terms = "legal_test_2026_01_01"
        )
    }
}
