package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text

interface LanguageAssetRepository {
    suspend fun loadAssets()
    suspend fun findAssets(query: String): List<Text>
    suspend fun getTranslation(id: String, lang: Language): String?
}
