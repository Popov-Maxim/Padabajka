package com.padabajka.dating.feature.push.notification.data.domain.model

import com.padabajka.dating.core.data.utils.mapToJson
import com.padabajka.dating.feature.push.data.domain.model.PlatformMessagePush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import platform.UserNotifications.UNNotification

class IosPlatformMessagePush(
    notification: UNNotification
) : PlatformMessagePush {
    private val userInfo = notification.request.content.userInfo as Map<String, Any?>

    override val dataJson: JsonObject
        get() = Json.mapToJson(userInfo)
}
