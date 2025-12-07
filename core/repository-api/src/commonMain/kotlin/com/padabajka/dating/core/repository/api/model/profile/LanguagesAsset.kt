package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class LanguagesAsset(
    val native: List<Text>,
    val known: List<Text>,
    val learning: List<Text>,
) {
    constructor() : this(
        native = emptyList(),
        known = emptyList(),
        learning = emptyList(),
    )
}
