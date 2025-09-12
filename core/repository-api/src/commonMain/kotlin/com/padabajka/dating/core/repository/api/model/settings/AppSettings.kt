package com.padabajka.dating.core.repository.api.model.settings

import com.padabajka.dating.core.repository.api.model.dictionary.Language
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val language: Language = Language.Static.EN,
)
