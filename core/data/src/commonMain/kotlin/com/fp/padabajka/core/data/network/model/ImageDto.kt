package com.fp.padabajka.core.data.network.model

import com.fp.padabajka.core.repository.api.model.profile.Image
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val url: String
)

fun ImageDto.toImage(): Image {
    return Image(
        url = url
    )
}
