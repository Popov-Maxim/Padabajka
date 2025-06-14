package com.padabajka.dating.feature.push.notification.data.domain.model

import com.padabajka.dating.core.data.utils.mapToJson
import com.padabajka.dating.feature.push.data.domain.model.PlatformDataPush
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import platform.UserNotifications.UNNotification

class IosPlatformDataPush(
    notification: UNNotification
) : PlatformDataPush {
    private val userInfo = notification.request.content.userInfo as Map<String, Any?>

    override val dataJson: JsonObject
        get() = Json.mapToJson(userInfo)
}
