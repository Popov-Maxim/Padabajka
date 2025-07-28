package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.dictionary.Language

interface DictionaryRepository {
    suspend fun loadDictionaries(selectedLang: String)
    suspend fun getText(id: String, type: String, lang: Language): String?
    fun getFastText(id: String, lang: Language): String?
}
