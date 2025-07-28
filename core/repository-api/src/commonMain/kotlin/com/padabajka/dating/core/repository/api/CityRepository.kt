package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.asset.City

interface CityRepository {
    suspend fun loadCities()
    suspend fun findCities(query: String): List<City>
    suspend fun getTranslation(id: String, lang: Language): String?
}
