package com.padabajka.dating.feature.dictionary.data.source

import com.padabajka.dating.core.repository.api.model.dictionary.Language

interface StaticWordDataSource {
    fun getWord(id: String, lang: Language.Static): String?
}
