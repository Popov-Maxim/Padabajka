package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.core.repository.api.model.profile.asset.City

interface AssetRepository {
    suspend fun loadAssets()
    suspend fun findCities(query: String): List<City>
    suspend fun findAssets(type: Text.Type, query: String): List<Text>

    suspend fun getTranslation(text: Text, lang: Language): String?
}
