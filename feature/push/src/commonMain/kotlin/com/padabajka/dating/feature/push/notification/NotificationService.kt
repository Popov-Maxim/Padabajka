package com.padabajka.dating.feature.push.notification

import com.padabajka.dating.core.permission.NotificationPermissionController
import com.padabajka.dating.feature.image.domain.LoadBitmapUseCase
import com.padabajka.dating.feature.push.notification.model.NotificationChannel
import com.padabajka.dating.feature.push.notification.platform.NotificationImportance
import com.padabajka.dating.feature.push.notification.platform.PlatformNotificationService

class NotificationService(
    private val platformService: PlatformNotificationService,
    private val notificationPermissionController: NotificationPermissionController,
    private val loadBitmapUseCase: LoadBitmapUseCase,
) {
    suspend fun showNotification(
        id: Int,
        title: String,
        message: String,
        channel: NotificationChannel,
        groupId: String? = null,
        deeplink: String? = null,
        iconUrl: String? = null
    ) {
        if (notificationPermissionController.hasPermission().not()) return

        val bitmap = iconUrl?.let { loadBitmapUseCase.invoke(it) }
        if (channel == NotificationChannel.Message) {
            platformService.showMessageNotification(
                chatId = id,
                chatName = title,
                message = message,
                channelId = channel.raw,
                groupId = groupId,
                priority = channel.priority(),
                deeplink = deeplink,
                chatIcon = bitmap
            )
        } else {
            platformService.showNotification(
                id = id,
                title = title,
                message = message,
                channelId = channel.raw,
                groupId = groupId,
                priority = channel.priority(),
                deeplink = deeplink,
                bitmap = bitmap
            )
        }
    }

    fun initNotificationChannels() {
        NotificationChannel.entries.forEach {
            platformService.createNotificationChannel(
                id = it.raw,
                name = it.name,
                description = it.description,
                importance = it.priority()
            )
        }
    }

    private fun NotificationChannel.priority() = when (this) {
        NotificationChannel.Message -> NotificationImportance.High
        NotificationChannel.Match -> NotificationImportance.Default
        NotificationChannel.Likes -> NotificationImportance.Low
    }
}
