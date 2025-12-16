package com.padabajka.dating.feature.messenger.data.message.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageSyncResponse(
    val messages: List<MessageDto>,
    val lastEventNumber: Long,
)
