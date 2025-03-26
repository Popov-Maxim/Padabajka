package com.padabajka.dating.core.repository.api.model.profile

sealed interface Image {
    data class Url(val value: String) : Image

    // TODO(Image): create ui image and move local in ui?
    data class Local(val data: ImageData) : Image
    data class ByteArray(val value: kotlin.ByteArray) : Image
}

fun Image.raw(): Any? {
    return when (this) {
        is Image.ByteArray -> value
        is Image.Local -> null
        is Image.Url -> value
    }
}
