package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.feature.profile.data.asset.model.InterestAssetDto
import com.padabajka.dating.feature.profile.data.asset.model.LanguageAssetDto
import com.padabajka.dating.feature.profile.data.asset.network.InterestAssetApi
import com.padabajka.dating.feature.profile.data.asset.network.LanguageAssetApi

class RemoteAssetDataSource(
    private val languageAssetApi: LanguageAssetApi,
    private val interestAssetApi: InterestAssetApi
) {
    suspend fun loadLanguages(): List<LanguageAssetDto> {
        return languageAssetApi.getAssets()
    }

    suspend fun loadInterestAssets(): List<InterestAssetDto> {
        return interestAssetApi.getAssets()
    }
}
