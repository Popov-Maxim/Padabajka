package com.padabajka.dating.feature.dictionary.data

import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.DictionaryRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.dictionary.data.source.StaticWordDataSource

class DictionaryRepositoryImpl(
    private val staticWordDataSource: StaticWordDataSource,
    private val assetRepository: AssetRepository,
) : DictionaryRepository {
    override suspend fun loadDictionaries(selectedLang: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getText(id: String, type: String, lang: Language): String? {
        return when (type) {
            Text.Type.City.raw,
            Text.Type.Language.raw -> {
                val text = Text(
                    id = Text.Id(id),
                    type = Text.Type.parse(type),
                )
                assetRepository.getTranslation(text, lang)
            }
            else -> {
                when (lang) {
                    is Language.Static -> staticWordDataSource.getWord(id, lang)
                    is Language.Dynamic -> TODO()
                }
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
