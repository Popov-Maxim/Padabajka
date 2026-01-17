package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.feature.profile.data.asset.model.toEntities
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteLanguageAssetDataSource

class LanguageAssetRepositoryImpl(
    private val remoteLanguageAssetDataSource: RemoteLanguageAssetDataSource,
    private val localAssetsDataSource: LocalAssetsDataSource,
) {
    suspend fun loadAssets() {
        val languagesDto = remoteLanguageAssetDataSource.loadLanguages()
        val entries = languagesDto.flatMap { it.toEntities() }
        localAssetsDataSource.setTranslations(entries)
    }
}
