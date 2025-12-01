package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.Lifestyle
import kotlinx.serialization.Serializable

@Serializable
data class LifestyleDto(
    val type: String,
    val value: TextDto?,
    val attributes: List<TextDto>
)

fun LifestyleDto.toDomain(): Lifestyle {
    return Lifestyle(
        type = type,
        value = value?.toText(),
        attributes = attributes.map { it.toText() }
    )
}

fun Lifestyle.toDto(): LifestyleDto {
    return LifestyleDto(
        type = type,
        value = value?.toTextDto(),
        attributes = attributes.map { it.toTextDto() }
    )
}
