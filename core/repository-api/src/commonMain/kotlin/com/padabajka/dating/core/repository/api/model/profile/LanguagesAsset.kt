package com.padabajka.dating.core.repository.api.model.profile

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
