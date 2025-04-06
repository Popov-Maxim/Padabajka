package com.padabajka.dating.feature.dictionary.data.source.static

import com.padabajka.dating.core.repository.api.model.dictionary.Language

class StaticDictionaryProvider {
    private val engDictionary = EngDictionary()
    private val ruDictionary = RuDictionary()

    fun getDictionary(lang: Language.Static): Dictionary {
        return when (lang) {
            Language.Static.EN -> engDictionary
            Language.Static.RU -> ruDictionary
        }
    }
}
