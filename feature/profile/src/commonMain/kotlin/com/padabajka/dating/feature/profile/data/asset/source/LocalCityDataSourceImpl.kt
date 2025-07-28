package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.component.room.asset.city.CityDao
import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.component.room.asset.city.entry.CityTranslation
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.feature.profile.data.asset.model.CityDB
import com.padabajka.dating.feature.profile.data.asset.model.toDomain

class LocalCityDataSourceImpl(
    private val cityDao: CityDao
) : LocalCityDataSource {
    override suspend fun setCities(cities: List<CityEntry>) {
        cityDao.insertCities(cities)
    }

    override suspend fun setCityTranslations(cities: List<CityTranslation>) {
        cityDao.insertCityTranslations(cities)
    }

    override suspend fun findCities(query: String, language: Language): List<CityDB> {
        return cityDao.findCityTranslations(query).mapNotNull { cityTranslation ->
            val id = cityTranslation.cityId
            val city = cityDao.getCity(id) ?: return@mapNotNull null
            val currentTranslation = cityDao.getCityTranslation(id, language.id) ?: cityTranslation
            city.toDomain(currentTranslation.name)
        }.distinct()
    }

    override suspend fun getCityTranslation(id: String, language: Language): CityTranslation? {
        return cityDao.getCityTranslation(id, language.id)
    }
}
