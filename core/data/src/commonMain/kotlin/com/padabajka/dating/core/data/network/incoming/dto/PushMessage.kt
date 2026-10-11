package com.padabajka.dating.core.data.network.incoming.dto

import com.padabajka.dating.core.repository.api.model.auth.UserId
import kotlinx.serialization.Serializable

@Serializable
data class PushMessage(
    val event: DataPush,
    val notification: NotificationPayload? = null,
    val recipientUserId: UserId? = null
)
