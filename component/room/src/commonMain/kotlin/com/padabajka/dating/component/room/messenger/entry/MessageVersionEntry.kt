package com.padabajka.dating.component.room.messenger.entry

import androidx.room.Entity

// Kept after deletion so a delayed event cannot restore the message.
@Entity(tableName = "message_versions", primaryKeys = ["chatId", "messageId"])
data class MessageVersionEntry(
    val chatId: String,
    val messageId: String,
    val version: Long?,
    val deleted: Boolean,
)
