package com.padabajka.dating.feature.push.notification.data.domain.model

import com.google.firebase.messaging.RemoteMessage
import com.padabajka.dating.core.data.utils.mapToJson
import com.padabajka.dating.feature.push.data.domain.model.PlatformMessagePush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class AndroidPlatformMessagePush(
    private val remoteMessage: RemoteMessage
) : PlatformMessagePush {
    override val dataJson: JsonObject
        get() = Json.mapToJson(remoteMessage.data)
}
