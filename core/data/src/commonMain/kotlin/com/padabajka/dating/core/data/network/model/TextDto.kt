package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.Text
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
data class TextDto(
    val id: Id,
    val type: String = "",
    val default: String? = null
) {
    @Serializable
    @JvmInline
    value class Id(val raw: String)
}

fun TextDto.Id.toTextId(): Text.Id = Text.Id(raw)
fun Text.Id.toTextIdDto(): TextDto.Id = TextDto.Id(raw)

fun TextDto.toText(): Text {
    return Text(
        id = id.toTextId(),
        type = Text.Type.parse(type),
        default = default
    )
}

fun Text.toTextDto(): TextDto {
    return TextDto(
        id = id.toTextIdDto(),
        type = type.raw,
        default = default
    )
}
