package com.padabajka.dating.core.repository.api.model.profile

data class Detail(
    val key: Text,
    val value: Value,
    val icon: ServerIcon
) {
    sealed interface Value {
        data class String(val raw: Text) : Value
        data class Centimeter(val raw: Int) : Value
    }
}
