package com.padabajka.dating.feature.push.notification.platform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class AndroidNotificationService(private val context: Context) : PlatformNotificationService {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification(
        id: Int,
        title: String,
        message: String,
        channelId: String,
        groupId: String?,
        priority: NotificationImportance?
    ) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(message)
            .apply {
                if (groupId != null) {
                    setGroup(groupId)
                }
            }
            .build()

        notificationManager.notify(id, notification)
    }

    override fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }

    override fun clearAllNotifications() {
        notificationManager.cancelAll()
    }

    override fun createNotificationChannel(
        id: String,
        name: String,
        description: String,
        importance: NotificationImportance
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                id,
                name,
                importance.toAndroidImportance()
            ).apply {
                this.description = description
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun NotificationImportance.toAndroidImportance() = when (this) {
        NotificationImportance.Low -> NotificationManager.IMPORTANCE_LOW
        NotificationImportance.Default -> NotificationManager.IMPORTANCE_DEFAULT
        NotificationImportance.High -> NotificationManager.IMPORTANCE_HIGH
    }
}
