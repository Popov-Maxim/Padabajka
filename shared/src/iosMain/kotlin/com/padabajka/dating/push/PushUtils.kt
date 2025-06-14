package com.padabajka.dating.push

import com.padabajka.dating.datapush.SharedPushHandler
import com.padabajka.dating.feature.push.notification.data.domain.model.IosPlatformDataPush
import platform.UserNotifications.UNNotification

fun SharedPushHandler.handlePush(notification: UNNotification) {
    val platformMessagePush = IosPlatformDataPush(notification)
    SharedPushHandler.handlePush(platformMessagePush)
}
