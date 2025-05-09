package com.padabajka.dating.feature.push.notification.platform

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
        priority: NotificationImportance?
    ) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(message)
            setSound(UNNotificationSound.defaultSound())

            priority?.toIosImportance()?.let { setInterruptionLevel(it) }
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
