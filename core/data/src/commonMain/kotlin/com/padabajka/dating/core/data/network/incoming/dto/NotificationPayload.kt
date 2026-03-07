package com.padabajka.dating.core.data.network.incoming.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationPayload(
    val id: Int,
    val channel: String,
    val groupId: String?,
    val title: String,
    val body: String,
    val deepLink: String
)
