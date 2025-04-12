package com.padabajka.dating.feature.dictionary.data.source

import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.feature.dictionary.data.source.static.StaticDictionaryProvider

class StaticWordDataSourceImpl(
    private val staticDictionaryProvider: StaticDictionaryProvider
) : StaticWordDataSource {
    override fun getWord(id: String, lang: Language.Static): String? {
        val textId = StaticTextId.parse(id) ?: return null
        val dictionary = staticDictionaryProvider.getDictionary(lang)
        return dictionary.getWord(textId)
    }
}
