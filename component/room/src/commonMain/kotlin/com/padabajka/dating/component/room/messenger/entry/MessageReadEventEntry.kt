package com.padabajka.dating.component.room.messenger.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.padabajka.dating.core.data.network.incoming.dto.MessageDataPush
import com.padabajka.dating.core.repository.api.model.auth.UserId

@Entity(
    tableName = "message_read_events",
    indices = [Index(value = ["chatId", "userId", "lastReadMessageId"], unique = true)]
)
data class MessageReadEventEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "chatId") val chatId: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "lastReadMessageId") val lastReadMessageId: String,
    @ColumnInfo(name = "lastReadMessageTime") val lastReadMessageTime: Long,
    @ColumnInfo(name = "readAt") val readAt: Long,
    @ColumnInfo(name = "readSynced") val readSynced: Boolean
)

fun messageReadEventEntry(
    chatId: String,
    userId: String,
    lastReadMessageId: String,
    lastReadMessageTime: Long,
    readAt: Long,
    readSynced: Boolean
) = MessageReadEventEntry(
    chatId = chatId,
    userId = userId,
    lastReadMessageId = lastReadMessageId,
    lastReadMessageTime = lastReadMessageTime,
    readAt = readAt,
    readSynced = readSynced
)

fun MessageEntry.toReadEventEntry(
    userId: UserId,
    readAt: Long,
    readSynced: Boolean
): MessageReadEventEntry {
    return messageReadEventEntry(
        chatId = chatId,
        userId = userId.raw,
        lastReadMessageId = id,
        lastReadMessageTime = creationTime,
        readAt = readAt,
        readSynced = readSynced
    )
}

fun MessageDataPush.ReadMessageEvent.toEntity(): MessageReadEventEntry {
    return messageReadEventEntry(
        chatId = chatId.raw,
        userId = userId.raw,
        lastReadMessageId = lastReadMessageId.raw,
        lastReadMessageTime = lastReadMessageTime,
        readAt = readAt,
        readSynced = true
    )
}
