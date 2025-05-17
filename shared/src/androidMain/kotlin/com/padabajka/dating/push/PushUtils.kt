package com.padabajka.dating.push

import com.google.firebase.messaging.RemoteMessage
import com.padabajka.dating.datapush.SharedPushHandler
import com.padabajka.dating.feature.push.notification.data.domain.model.AndroidPlatformMessagePush

fun SharedPushHandler.handlePush(remoteMessage: RemoteMessage) {
    val platformMessagePush = AndroidPlatformMessagePush(remoteMessage)
    SharedPushHandler.handlePush(platformMessagePush)
}
