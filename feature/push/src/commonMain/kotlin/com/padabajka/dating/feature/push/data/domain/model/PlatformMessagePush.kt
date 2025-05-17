package com.padabajka.dating.feature.push.data.domain.model

import kotlinx.serialization.json.JsonObject

interface PlatformMessagePush {
    val dataJson: JsonObject
}
