package com.padabajka.dating.feature.push.notification.platform

import coil3.Bitmap

interface PlatformNotificationService {
    fun showNotification(
        id: Int,
        title: String,
        message: String,
        channelId: String,
        groupId: String? = null,
        priority: NotificationImportance,
        deeplink: String? = null,
        bitmap: Bitmap? = null
    )

    fun showMessageNotification(
        chatId: Int,
        chatName: String,
        message: String,
        channelId: String,
        groupId: String? = null,
        priority: NotificationImportance,
        deeplink: String? = null,
        chatIcon: Bitmap? = null
    )

    fun cancelNotification(id: Int)
    fun clearAllNotifications()

    fun createNotificationChannel(
        id: String,
        name: String,
        description: String,
        importance: NotificationImportance
    )
}

enum class NotificationImportance {
    Low, Default, High
}
