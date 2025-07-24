package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.component.room.asset.city.entry.CityTranslation
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.feature.profile.data.asset.model.CityDB

interface LocalCityDataSource {
    suspend fun setCities(cities: List<CityEntry>)
    suspend fun setCityTranslations(cities: List<CityTranslation>)
    suspend fun findCities(query: String, language: Language): List<CityDB>
}
