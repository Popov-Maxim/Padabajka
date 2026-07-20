package com.padabajka.dating.settings.presentation.model

import com.padabajka.dating.core.presentation.State
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.legal.LegalVersions

data class SettingsState(
    val selectedLanguage: Language.Static,
    val subscriptionActive: Boolean,
    val profileFrozen: Boolean,
    val legalVersions: LegalVersions
) : State
