package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.dictionary.Language

interface DictionaryRepository {
    suspend fun loadDictionaries(selectedLang: String)
    fun getText(id: String, lang: Language): String?
}
