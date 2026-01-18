package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.component.room.asset.entry.AssetsTranslationEntry
import com.padabajka.dating.feature.profile.data.asset.model.toEntity
import com.padabajka.dating.feature.profile.data.asset.model.toTranslationEntity
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSource

class CityRepositoryImpl(
    private val remoteCityDataSource: RemoteCityDataSource,
    private val localCityDataSource: LocalCityDataSource
) {
    suspend fun loadCities(): List<AssetsTranslationEntry> {
        val citiesDto = remoteCityDataSource.loadCities()
        val cityEntries = citiesDto.map { it.toEntity() }
        val cityTranslationEntries = citiesDto.flatMap { it.toTranslationEntity() }
        localCityDataSource.setCities(cityEntries)

        return cityTranslationEntries
    }
}
