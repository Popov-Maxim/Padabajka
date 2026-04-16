package com.padabajka.dating.core.repository.api.model.profile

import com.padabajka.dating.core.repository.api.model.common.CoreRect
import kotlinx.serialization.Serializable

@Serializable
sealed interface Image {
    @Serializable
    data class Url(val value: String) : Image

    // TODO(Image): create ui image and move local in ui?
    @Serializable
    data class Local(val data: ImageData, val rect: CoreRect? = null) : Image
    data class ByteArray(val value: kotlin.ByteArray) : Image
}

@Serializable
data class Size(
    val width: Int,
    val height: Int
)
