package com.padabajka.dating

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.padabajka.dating.datapush.SharedPushHandler

class PushFirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data.let { data ->
            SharedPushHandler.handlePush(data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SharedPushHandler.saveToken(token)
    }
}
