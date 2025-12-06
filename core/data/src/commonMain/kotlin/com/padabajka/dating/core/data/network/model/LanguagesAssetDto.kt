package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.LanguagesAsset
import kotlinx.serialization.Serializable

@Serializable
data class LanguagesAssetDto(
    val native: List<TextDto>,
    val known: List<TextDto>,
    val learning: List<TextDto>,
)

fun LanguagesAsset.toDto(): LanguagesAssetDto {
    return LanguagesAssetDto(
        native = native.map { it.toTextDto() },
        known = known.map { it.toTextDto() },
        learning = learning.map { it.toTextDto() },
    )
}

fun LanguagesAssetDto.toDomain(): LanguagesAsset {
    return LanguagesAsset(
        native = native.map { it.toText() },
        known = known.map { it.toText() },
        learning = learning.map { it.toText() },
    )
}
