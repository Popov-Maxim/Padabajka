package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.feature.profile.data.asset.model.AssetsDto
import com.padabajka.dating.feature.profile.data.asset.model.InterestAssetDto
import com.padabajka.dating.feature.profile.data.asset.model.LanguageAssetDto
import com.padabajka.dating.feature.profile.data.asset.network.InterestAssetApi
import com.padabajka.dating.feature.profile.data.asset.network.LanguageAssetApi

class RemoteAssetDataSource(
    private val languageAssetApi: LanguageAssetApi,
    private val interestAssetApi: InterestAssetApi
) {
    suspend fun loadLanguages(clientVersion: Int): AssetsDto<LanguageAssetDto>? {
        return languageAssetApi.getAssets(clientVersion)
    }

    suspend fun loadInterestAssets(clientVersion: Int): AssetsDto<InterestAssetDto>? {
        return interestAssetApi.getAssets(clientVersion)
    }
}
