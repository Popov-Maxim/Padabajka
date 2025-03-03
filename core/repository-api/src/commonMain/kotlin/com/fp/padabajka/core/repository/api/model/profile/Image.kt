package com.fp.padabajka.core.repository.api.model.profile

sealed interface Image {
    data class Url(val value: String) : Image
    data class LocalUri(val value: String) : Image
    data class ByteArray(val value: kotlin.ByteArray) : Image
}

fun Image.raw(): Any {
    return when (this) {
        is Image.ByteArray -> value
        is Image.LocalUri -> value
        is Image.Url -> value
    }
}
