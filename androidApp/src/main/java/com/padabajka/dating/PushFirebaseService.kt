package com.padabajka.dating

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.padabajka.dating.datapush.SharedPushHandler
import com.padabajka.dating.push.handlePush

class PushFirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        println("LOG: push onMessageReceived")
        SharedPushHandler.handlePush(remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SharedPushHandler.saveToken(token)
    }
}
