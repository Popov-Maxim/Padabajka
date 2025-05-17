package com.padabajka.dating.push

import com.padabajka.dating.datapush.SharedPushHandler
import com.padabajka.dating.feature.push.notification.data.domain.model.IosPlatformMessagePush
import platform.UserNotifications.UNNotification

fun SharedPushHandler.handlePush(notification: UNNotification) {
    val platformMessagePush = IosPlatformMessagePush(notification)
    SharedPushHandler.handlePush(platformMessagePush)
}
