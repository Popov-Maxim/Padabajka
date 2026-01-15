package com.padabajka.dating.settings.presentation.model.language

import com.padabajka.dating.core.repository.api.model.dictionary.Language

sealed interface LanguageEvent {
    data object NavigateBack : LanguageEvent
    data class ChangeLanguage(val language: Language.Static) : LanguageEvent
}
