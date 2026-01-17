package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.feature.profile.data.asset.model.toEntity
import com.padabajka.dating.feature.profile.data.asset.model.toTranslationEntity
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSource

class CityRepositoryImpl(
    private val remoteCityDataSource: RemoteCityDataSource,
    private val localCityDataSource: LocalCityDataSource,
    private val localAssetsDataSource: LocalAssetsDataSource
) {
    suspend fun loadCities() {
        val citiesDto = remoteCityDataSource.loadCities()
        val cityEntries = citiesDto.map { it.toEntity() }
        val cityTranslationEntries = citiesDto.flatMap { it.toTranslationEntity() }
        localCityDataSource.setCities(cityEntries)
        localAssetsDataSource.setTranslations(cityTranslationEntries)
    }
}
