package com.padabajka.dating.feature.push.notification.platform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri

class AndroidNotificationService(private val context: Context) : PlatformNotificationService {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification(
        id: Int,
        title: String,
        message: String,
        channelId: String,
        groupId: String?,
        priority: NotificationImportance?,
        deeplink: String?
    ) {
        val intent = createPendingIntentForDeepLink(context, id, deeplink)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .apply {
                if (groupId != null) {
                    setGroup(groupId)
                }
            }
            .build()

        notificationManager.notify(id, notification)
    }

    private fun createPendingIntentForDeepLink(
        context: Context,
        notificationId: Int,
        deepLink: String?
    ): PendingIntent {
        val intent = deepLink?.let {
            Intent(Intent.ACTION_VIEW, safeParseUri(deepLink))
        }
            ?: context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: Intent()

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        return PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun safeParseUri(uri: String): Uri? {
        return runCatching { uri.toUri() }.getOrNull()
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
