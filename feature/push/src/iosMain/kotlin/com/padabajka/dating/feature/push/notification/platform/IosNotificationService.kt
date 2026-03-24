package com.padabajka.dating.feature.push.notification.platform

import coil3.Bitmap
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationInterruptionLevel
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

class IosNotificationService : PlatformNotificationService {

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
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setSound(UNNotificationSound.defaultSound())

            setInterruptionLevel(priority.toIosImportance())
            groupId?.let {
                setThreadIdentifier(it)
            }
            deeplink?.let {
                setUserInfo(mapOf("deeplink" to it))
            }
        }

        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = id.toString(),
            content = content,
            trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                timeInterval = Double.MIN_VALUE,
                repeats = false
            )
        )

        UNUserNotificationCenter.currentNotificationCenter().apply {
            addNotificationRequest(request, null)
        }
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
        showNotification(
            id = chatId,
            title = chatName,
            message = message,
            channelId = channelId,
            groupId = groupId,
            priority = priority,
            deeplink = deeplink,
            bitmap = chatIcon
        )
    }

    override fun cancelNotification(id: Int) {
        UNUserNotificationCenter.currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(listOf(id.toString()))
    }

    override fun clearAllNotifications() {
        UNUserNotificationCenter.currentNotificationCenter().apply {
            removeAllPendingNotificationRequests()
            removeAllDeliveredNotifications()
        }
    }

    override fun createNotificationChannel(
        id: String,
        name: String,
        description: String,
        importance: NotificationImportance
    ) {
        // iOS dont support notification channels
    }

    private fun NotificationImportance.toIosImportance() = when (this) {
        NotificationImportance.Low -> UNNotificationInterruptionLevel.UNNotificationInterruptionLevelPassive
        NotificationImportance.Default -> UNNotificationInterruptionLevel.UNNotificationInterruptionLevelActive
        NotificationImportance.High -> UNNotificationInterruptionLevel.UNNotificationInterruptionLevelTimeSensitive
    }
}
