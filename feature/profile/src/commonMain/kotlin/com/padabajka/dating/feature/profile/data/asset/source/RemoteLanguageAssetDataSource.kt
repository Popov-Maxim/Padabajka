package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.feature.profile.data.asset.model.LanguageAssetDto
import com.padabajka.dating.feature.profile.data.asset.network.LanguageAssetApi

class RemoteLanguageAssetDataSource(
    private val languageAssetApi: LanguageAssetApi
) {
    suspend fun loadLanguages(): List<LanguageAssetDto> {
        return languageAssetApi.getAssets()
    }
}
