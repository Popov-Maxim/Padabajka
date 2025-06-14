package com.padabajka.dating.feature.push.data.domain.model

import kotlinx.serialization.json.JsonObject

interface MessagePush {
    val dataJson: JsonObject

    data class Json(override val dataJson: JsonObject) : MessagePush
}
