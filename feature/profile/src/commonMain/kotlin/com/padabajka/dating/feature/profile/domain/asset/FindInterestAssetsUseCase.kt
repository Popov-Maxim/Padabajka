package com.padabajka.dating.feature.profile.domain.asset

import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.model.profile.Text

class FindInterestAssetsUseCase(
    private val repository: AssetRepository
) {
    suspend operator fun invoke(query: String): List<Text> {
        return repository.findAssets(Text.Type.Interest, query)
    }
}
