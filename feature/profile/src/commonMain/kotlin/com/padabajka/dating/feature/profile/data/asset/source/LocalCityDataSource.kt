package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.component.room.asset.city.entry.CityEntry

interface LocalCityDataSource {
    suspend fun setCities(cities: List<CityEntry>)
    suspend fun getCity(id: String): CityEntry?
}
