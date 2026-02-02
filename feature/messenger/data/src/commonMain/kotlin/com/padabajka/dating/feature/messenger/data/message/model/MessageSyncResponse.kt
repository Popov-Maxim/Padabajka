package com.padabajka.dating.feature.messenger.data.message.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageSyncResponse(
    val lastEventNumber: Long,
    val messages: List<MessageDto>,
    val lastReadEventLogNumber: Long,
    val readEvents: List<ChatReadEventResponse>,
)
