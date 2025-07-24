package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.core.repository.api.CityRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.asset.City
import com.padabajka.dating.feature.profile.data.asset.model.toDomain
import com.padabajka.dating.feature.profile.data.asset.model.toEntity
import com.padabajka.dating.feature.profile.data.asset.model.toTranslationEntity
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteCityDataSource

class CityRepositoryImpl(
    private val remoteCityDataSource: RemoteCityDataSource,
    private val localCityDataSource: LocalCityDataSource,
) : CityRepository {
    override suspend fun loadCities() {
        val citiesDto = remoteCityDataSource.loadCities()
        val cityEntries = citiesDto.map { it.toEntity() }
        val cityTranslationEntries = citiesDto.flatMap { it.toTranslationEntity() }
        localCityDataSource.setCities(cityEntries)
        localCityDataSource.setCityTranslations(cityTranslationEntries)
    }

    override suspend fun findCities(query: String): List<City> {
        val cityEntries = localCityDataSource.findCities(query, Language.Static.EN)
        return cityEntries.map { it.toDomain() }
    }
}
