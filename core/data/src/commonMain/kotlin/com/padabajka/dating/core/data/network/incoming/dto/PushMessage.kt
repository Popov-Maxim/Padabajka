package com.padabajka.dating.core.data.network.incoming.dto

import kotlinx.serialization.Serializable

@Serializable
data class PushMessage(
    val event: DataPush,
    val notification: NotificationPayload? = null
)
