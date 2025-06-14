package com.padabajka.dating.feature.push.notification.data.domain.model

import com.google.firebase.messaging.RemoteMessage
import com.padabajka.dating.core.data.utils.mapToJson
import com.padabajka.dating.feature.push.data.domain.model.PlatformDataPush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class AndroidPlatformDataPush(
    private val remoteMessage: RemoteMessage
) : PlatformDataPush {
    override val dataJson: JsonObject
        get() = Json.mapToJson(remoteMessage.data)
}
