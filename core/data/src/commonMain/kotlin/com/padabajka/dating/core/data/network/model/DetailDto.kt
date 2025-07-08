package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.Detail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailDto(
    val type: String,
    val value: Value
) {
    @Serializable
    sealed interface Value {

        @Serializable
        @SerialName("string")
        data class String(val raw: kotlin.String) : Value

        @Serializable
        @SerialName("asset")
        data class Asset(val raw: TextDto) : Value

        @Serializable
        @SerialName("centimeter")
        data class Centimeter(val raw: Double) : Value
    }
}

fun DetailDto.toDomain(): Detail {
    return Detail(
        type = type,
        value = value.toDomain(),
        icon = null
    )
}

fun Detail.toDto(): DetailDto {
    return DetailDto(
        type = type,
        value = value.toDto(),
    )
}

private fun DetailDto.Value.toDomain(): Detail.Value {
    return when (this) {
        is DetailDto.Value.Centimeter -> Detail.Value.Centimeter(raw)
        is DetailDto.Value.String -> Detail.Value.String(raw)
        is DetailDto.Value.Asset -> Detail.Value.Asset(raw.toText())
    }
}

private fun Detail.Value.toDto(): DetailDto.Value {
    return when (this) {
        is Detail.Value.Asset -> DetailDto.Value.Asset(raw.toTextDto())
        is Detail.Value.Centimeter -> DetailDto.Value.Centimeter(raw)
        is Detail.Value.String -> DetailDto.Value.String(raw)
    }
}
