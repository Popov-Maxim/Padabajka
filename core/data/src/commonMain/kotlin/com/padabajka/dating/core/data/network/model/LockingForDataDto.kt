package com.padabajka.dating.core.data.network.model

import com.padabajka.dating.core.repository.api.model.profile.LockingForData
import kotlinx.serialization.Serializable

@Serializable
class LockingForDataDto(
    val type: TextDto,
    val detail: TextDto? = null
)

fun LockingForDataDto.toLockingForData(): LockingForData {
    return LockingForData(
        type = type.toText(),
        detail = detail?.toText()
    )
}
