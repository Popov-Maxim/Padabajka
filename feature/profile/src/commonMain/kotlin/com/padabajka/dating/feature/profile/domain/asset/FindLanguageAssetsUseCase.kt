package com.padabajka.dating.feature.profile.domain.asset

import com.padabajka.dating.core.repository.api.LanguageAssetRepository
import com.padabajka.dating.core.repository.api.model.profile.Text

class FindLanguageAssetsUseCase(
    private val repository: LanguageAssetRepository
) {
    suspend operator fun invoke(query: String): List<Text> {
        return repository.findAssets(query)
    }
}
