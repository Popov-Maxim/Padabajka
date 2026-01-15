package com.padabajka.dating.settings.presentation.model.language

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import kotlinx.collections.immutable.PersistentList

data class LanguageState(
    val selectedLanguage: Language.Static,
    val languages: PersistentList<LanguageItem> = supportedLanguages
) : State
