package com.padabajka.dating.feature.profile.data.asset.model

import com.padabajka.dating.component.room.asset.language.entry.LanguageTranslationEntry
import kotlinx.serialization.Serializable

@Serializable
data class LanguageAssetDto(
    val id: String,
    val translations: Map<String, String>
)

fun LanguageAssetDto.toEntities(): List<LanguageTranslationEntry> {
    return translations.map { (language, translation) ->
        LanguageTranslationEntry(
            id = id,
            language = language,
            name = translation
        )
    }
}
