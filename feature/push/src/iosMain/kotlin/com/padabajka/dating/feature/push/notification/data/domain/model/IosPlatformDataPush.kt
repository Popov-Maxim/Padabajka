package com.padabajka.dating.feature.push.notification.data.domain.model

import com.padabajka.dating.core.utils.safeCast
import com.padabajka.dating.feature.push.data.domain.model.PlatformDataPush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import platform.UserNotifications.UNNotification

class IosPlatformDataPush(
    notification: UNNotification
) : PlatformDataPush {
    private val userInfo = notification.request.content.userInfo

    override val dataJson: JsonObject
        get() = userInfo["payload"]?.safeCast<String>()?.let { payload ->
            Json.parseToJsonElement(payload).jsonObject
        } ?: JsonObject(emptyMap())
}
