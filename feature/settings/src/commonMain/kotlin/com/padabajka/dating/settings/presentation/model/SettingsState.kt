package com.padabajka.dating.settings.presentation.model

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.dictionary.Language

data class SettingsState(
    val selectedLanguage: Language.Static,
) : State
