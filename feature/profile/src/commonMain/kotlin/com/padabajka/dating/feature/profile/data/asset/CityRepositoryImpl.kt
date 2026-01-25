package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.feature.profile.data.asset.model.AssetVersionsDto
import com.padabajka.dating.feature.profile.data.asset.model.toEntity
import com.padabajka.dating.feature.profile.data.asset.model.toTranslationEntity
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetVersionsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSource

class CityRepositoryImpl(
    private val remoteCityDataSource: RemoteCityDataSource,
    private val localCityDataSource: LocalCityDataSource,
    private val localAssetDataSource: LocalAssetsDataSource,
    private val localAssetVersionsDataSource: LocalAssetVersionsDataSource,
) {
    suspend fun loadCities(clientVersion: Int) {
        val citiesDto = remoteCityDataSource.loadCities(clientVersion) ?: return
        val cityEntries = citiesDto.assets.map { it.toEntity() }
        val cityTranslationEntries = citiesDto.assets.flatMap { it.toTranslationEntity() }
        localCityDataSource.setCities(cityEntries)

        localAssetDataSource.setTranslations(cityTranslationEntries)
        localAssetVersionsDataSource.updateVersion(AssetVersionsDto.Type.Cities, citiesDto.version)
    }
}
