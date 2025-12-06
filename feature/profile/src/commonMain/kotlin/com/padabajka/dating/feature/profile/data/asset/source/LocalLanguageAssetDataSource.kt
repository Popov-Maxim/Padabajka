package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.component.room.asset.language.LanguageDao
import com.padabajka.dating.component.room.asset.language.entry.LanguageTranslationEntry
import com.padabajka.dating.core.repository.api.model.dictionary.Language

class LocalLanguageAssetDataSource(
    private val languageDao: LanguageDao
) {
    suspend fun setTranslations(cities: List<LanguageTranslationEntry>) {
        languageDao.insertLanguageTranslations(cities)
    }

    suspend fun find(query: String, language: Language): List<LanguageTranslationEntry> {
        return languageDao.findLangAssets(query, language.id)
    }
    suspend fun getLanguage(id: String, language: Language): LanguageTranslationEntry? {
        return languageDao.getCityTranslation(id, language.id)
    }
}
