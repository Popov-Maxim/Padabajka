package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.Lifestyle
import kotlinx.serialization.Serializable

@Serializable
data class LifestyleDto(
    val type: TextDto,
    val value: Value,
    val attitude: TextDto
) {
    @Serializable
    data class Value(
        val frequency: TextDto? = null,
        val specifics: List<TextDto> = emptyList()
    )
}

fun LifestyleDto.toDomain(): Lifestyle {
    return Lifestyle(
        type = type.toText(),
        value = value.toDomain(),
        attitude = attitude.toText()
    )
}

private fun LifestyleDto.Value.toDomain(): Lifestyle.Value {
    return Lifestyle.Value(
        frequency = frequency?.toText(),
        specifics = specifics.map { it.toText() }
    )
}
