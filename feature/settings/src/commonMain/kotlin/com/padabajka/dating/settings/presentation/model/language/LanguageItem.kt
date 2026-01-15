package com.padabajka.dating.settings.presentation.model.language

import com.padabajka.dating.core.repository.api.model.dictionary.Language
import kotlinx.collections.immutable.persistentListOf

data class LanguageItem(
    val id: Language.Static,
    val name: String,
)

val supportedLanguages = persistentListOf(
    LanguageItem(id = Language.Static.EN, name = "English"),
    LanguageItem(id = Language.Static.RU, name = "Русский")
)

val supportedLanguagesMap = supportedLanguages.associateBy { it.id }
