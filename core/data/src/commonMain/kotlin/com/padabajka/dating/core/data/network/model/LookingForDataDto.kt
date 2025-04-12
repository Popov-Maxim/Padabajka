package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import kotlinx.serialization.Serializable

@Serializable
class LookingForDataDto(
    val type: TextDto,
    val detail: TextDto? = null
)

fun LookingForDataDto.toLookingForData(): LookingForData {
    return LookingForData(
        type = type.toText(),
        detail = detail?.toText()
    )
}

fun LookingForData.toLookingForDataDto(): LookingForDataDto {
    return LookingForDataDto(
        type = type.toTextDto(),
        detail = detail?.toTextDto()
    )
}
