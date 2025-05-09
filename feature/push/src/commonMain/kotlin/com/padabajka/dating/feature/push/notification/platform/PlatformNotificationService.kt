package com.padabajka.dating.feature.push.notification.platform

interface PlatformNotificationService {
    fun showNotification(
        id: Int,
        title: String,
        message: String,
        channelId: String,
        groupId: String? = null,
        priority: NotificationImportance? = null
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
