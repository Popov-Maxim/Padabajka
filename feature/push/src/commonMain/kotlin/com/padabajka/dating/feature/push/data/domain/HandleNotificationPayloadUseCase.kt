package com.padabajka.dating.feature.push.data.domain

import com.padabajka.dating.core.data.network.incoming.dto.NotificationPayload
import com.padabajka.dating.feature.push.notification.NotificationService
import com.padabajka.dating.feature.push.notification.model.NotificationChannel

class HandleNotificationPayloadUseCase(
    private val notificationService: NotificationService
) {
    suspend operator fun invoke(payload: NotificationPayload) {
        val channel = payload.channel.run(NotificationChannel::parse) ?: return
        notificationService.showNotification(
            id = payload.id,
            title = payload.title,
            message = payload.body,
            channel = channel
        )
    }
}
