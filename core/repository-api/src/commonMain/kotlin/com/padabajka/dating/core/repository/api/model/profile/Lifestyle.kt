package com.padabajka.dating.core.repository.api.model.profile

data class Lifestyle(
    val type: Text,
    val value: Value,
    val attitude: Text
) {
    data class Value(
        val frequency: Text?,
        val specifics: List<Text>
    )
}
