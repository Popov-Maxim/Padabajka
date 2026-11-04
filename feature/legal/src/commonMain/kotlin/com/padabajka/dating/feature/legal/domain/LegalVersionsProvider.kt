package com.padabajka.dating.feature.legal.domain

import com.padabajka.dating.core.domain.config.LegalConfig
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
            privacy = LegalConfig.DEFAULT_PRIVACY_VERSION,
            terms = LegalConfig.DEFAULT_TERMS_VERSION
        )
    }
}
