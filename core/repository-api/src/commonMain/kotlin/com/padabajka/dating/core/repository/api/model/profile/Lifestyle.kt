package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class Lifestyle(
    val type: Text,
    val value: Value,
    val attitude: Text
) {
    @Serializable
    data class Value(
        val frequency: Text?,
        val specifics: List<Text>
    )
}
