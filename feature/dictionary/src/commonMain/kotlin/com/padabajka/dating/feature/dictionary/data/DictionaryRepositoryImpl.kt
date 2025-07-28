package com.padabajka.dating.feature.dictionary.data

import com.padabajka.dating.core.repository.api.CityRepository
import com.padabajka.dating.core.repository.api.DictionaryRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.dictionary.data.source.StaticWordDataSource

class DictionaryRepositoryImpl(
    private val staticWordDataSource: StaticWordDataSource,
    private val cityRepository: CityRepository
) : DictionaryRepository {
    override suspend fun loadDictionaries(selectedLang: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getText(id: String, type: String, lang: Language): String? {
        return if (type == Text.Type.City.raw) {
            cityRepository.getTranslation(id, lang)
        } else {
            when (lang) {
                is Language.Static -> staticWordDataSource.getWord(id, lang)
                is Language.Dynamic -> TODO()
            }
        }
    }

    override fun getFastText(id: String, lang: Language): String? {
        return when (lang) {
            is Language.Static -> staticWordDataSource.getWord(id, lang)
            is Language.Dynamic -> TODO()
        }
    }
}
