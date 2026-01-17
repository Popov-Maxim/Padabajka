package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.component.room.asset.city.CityDao
import com.padabajka.dating.component.room.asset.city.entry.CityEntry

class LocalCityDataSourceImpl(
    private val cityDao: CityDao
) : LocalCityDataSource {
    override suspend fun setCities(cities: List<CityEntry>) {
        cityDao.insertCities(cities)
    }

    override suspend fun getCity(id: String): CityEntry? {
        return cityDao.getCity(id)
    }
}
