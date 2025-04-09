package com.padabajka.dating.feature.dictionary.data

import com.padabajka.dating.core.repository.api.DictionaryRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.feature.dictionary.data.source.StaticWordDataSource

class DictionaryRepositoryImpl(
    private val staticWordDataSource: StaticWordDataSource
) : DictionaryRepository {
    override suspend fun loadDictionaries(selectedLang: String) {
        TODO("Not yet implemented")
    }

    override fun getText(id: String, lang: Language): String? {
        return when (lang) {
            is Language.Static -> staticWordDataSource.getWord(id, lang)
            is Language.Dynamic -> TODO()
        }
    }
}
