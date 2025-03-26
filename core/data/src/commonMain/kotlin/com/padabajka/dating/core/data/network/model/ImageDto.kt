package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.Image
import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val url: String
)

fun ImageDto.toImage(): Image.Url {
    return Image.Url(
        value = url
    )
}

fun Image.Url.toImageDto(): ImageDto {
    return ImageDto(
        url = value
    )
}
