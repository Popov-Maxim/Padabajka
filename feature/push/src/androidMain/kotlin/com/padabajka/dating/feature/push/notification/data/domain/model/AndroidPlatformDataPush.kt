package com.padabajka.dating.feature.push.notification.data.domain.model

import com.google.firebase.messaging.RemoteMessage
import com.padabajka.dating.feature.push.data.domain.model.PlatformDataPush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class AndroidPlatformDataPush(
    private val remoteMessage: RemoteMessage
) : PlatformDataPush {
    override val dataJson: JsonObject
        get() = remoteMessage.data["payload"]?.let { payload ->
            Json.parseToJsonElement(payload).jsonObject
        } ?: JsonObject(emptyMap())
}
