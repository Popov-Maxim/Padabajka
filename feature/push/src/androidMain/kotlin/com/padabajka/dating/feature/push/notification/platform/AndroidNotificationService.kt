package com.padabajka.dating.feature.push.notification.platform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import coil3.Bitmap

class AndroidNotificationService(private val context: Context) : PlatformNotificationService {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification(
        id: Int,
        title: String,
        message: String,
        channelId: String,
        groupId: String?,
        priority: NotificationImportance,
        deeplink: String?,
        bitmap: Bitmap?
    ) {
        val notification = NotificationCompat.Builder(context, channelId)
            .commonConfig(id, title, message, priority, deeplink)
            .build()

        notificationManager.notify(id, notification)
    }

    override fun showMessageNotification(
        chatId: Int,
        chatName: String,
        message: String,
        channelId: String,
        groupId: String?,
        priority: NotificationImportance,
        deeplink: String?,
        chatIcon: Bitmap?
    ) {
        val style = createMessagingStyle(chatName, message, chatIcon)

        val notification = NotificationCompat.Builder(context, channelId)
            .commonConfig(chatId, chatName, message, priority, deeplink)
            .setStyle(style)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager.notify(chatId, notification)
    }

    private fun NotificationCompat.Builder.commonConfig(
        title: String,
        text: String,
        priority: NotificationImportance,
        intent: PendingIntent
    ): NotificationCompat.Builder {
        return this
            .setSmallIcon(com.padabajka.dating.feature.push.R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .setPriority(priority.toAndroidImportance())
    }

    private fun NotificationCompat.Builder.commonConfig(
        id: Int,
        title: String,
        text: String,
        priority: NotificationImportance,
        deeplink: String?
    ): NotificationCompat.Builder {
        val intent = createPendingIntentForDeepLink(context, id, deeplink)

        return commonConfig(title, text, priority, intent)
    }

    private fun createMessagingStyle(
        senderName: String,
        message: String,
        bitmap: Bitmap?,
    ): NotificationCompat.MessagingStyle {
        val person = Person.Builder()
            .setName(senderName)
            .setIcon(
                bitmap?.let {
                    IconCompat.createWithBitmap(it)
                }
            )
            .build()

        val style = NotificationCompat.MessagingStyle(person)
            .addMessage(
                NotificationCompat.MessagingStyle.Message(
                    message,
                    System.currentTimeMillis(),
                    person
                )
            )

        return style
    }

    private fun createIntentForDeepLink(
        deepLink: String?
    ): Intent {
        val intent = deepLink?.let {
            Intent(Intent.ACTION_VIEW, safeParseUri(deepLink))
        }
            ?: context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?: Intent()

        return intent
    }

    private fun createPendingIntentForDeepLink(
        context: Context,
        notificationId: Int,
        deepLink: String?
    ): PendingIntent {
        val intent = createIntentForDeepLink(deepLink)

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
