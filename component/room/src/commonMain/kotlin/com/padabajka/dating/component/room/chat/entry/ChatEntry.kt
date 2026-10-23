package com.padabajka.dating.component.room.chat.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.padabajka.dating.core.repository.api.model.messenger.Chat
import com.padabajka.dating.core.repository.api.model.messenger.ChatId
import com.padabajka.dating.core.repository.api.model.messenger.MessageId

@Entity(tableName = "chats")
data class ChatEntry(
    @PrimaryKey val id: String,
    @ColumnInfo("lastEventNumber") val lastEventNumber: Long,
    @ColumnInfo("lastReadEventNumber") val lastReadEventNumber: Long,
    @ColumnInfo("hasMoreOldMessages") val hasMoreOldMessages: Boolean,
    @ColumnInfo("historyBeforeMessageId") val historyBeforeMessageId: String? = null,
)

fun ChatEntry.toDomain(): Chat {
    return Chat(
        id = id.run(::ChatId),
        lastEventNumber = lastEventNumber,
        lastReadEventNumber = lastReadEventNumber,
        hasMoreOldMessages = hasMoreOldMessages,
        historyBeforeMessageId = historyBeforeMessageId?.let(::MessageId),
    )
}

fun Chat.toDB(): ChatEntry {
    return ChatEntry(
        id = id.raw,
        lastEventNumber = lastEventNumber,
        lastReadEventNumber = lastReadEventNumber,
        hasMoreOldMessages = hasMoreOldMessages,
        historyBeforeMessageId = historyBeforeMessageId?.raw,
    )
}
