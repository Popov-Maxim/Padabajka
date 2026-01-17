package com.padabajka.dating.feature.profile.data.asset.model

import com.padabajka.dating.component.room.asset.entry.AssetsTranslationEntry
import com.padabajka.dating.core.repository.api.model.profile.Text
import kotlinx.serialization.Serializable

@Serializable
data class LanguageAssetDto(
    val id: String,
    val translations: Map<String, String>
)

fun LanguageAssetDto.toEntities(): List<AssetsTranslationEntry> {
    return translations.map { (language, translation) ->
        AssetsTranslationEntry(
            id = id,
            type = Text.Type.Language.raw,
            language = language,
            name = translation,
        )
    }
}
