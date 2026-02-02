package com.padabajka.dating.component.room.messenger.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.padabajka.dating.core.repository.api.model.messenger.MessageReaction
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import kotlinx.serialization.Serializable

@Entity(tableName = "messages")
data class MessageEntry(
    @PrimaryKey val id: String,
    @ColumnInfo("chatId") val chatId: String,
    @ColumnInfo("authorId") val authorId: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("creationTime") val creationTime: Long,
    @ColumnInfo("reaction") val reactions: List<MessageReactionEntity>?,
    @ColumnInfo("messageStatus") val messageStatus: MessageStatus,
    @ColumnInfo("readSynced") val readSynced: Boolean,
    @ColumnInfo("editedAt") val editedAt: Long?,
    @ColumnInfo("editSynced") val editSynced: Boolean,
    @ColumnInfo("parentMessageId") val parentMessageId: String?
)

@Serializable
data class MessageReactionEntity(
    val author: String,
    val value: MessageReaction.Value,
    val time: Long,
    val reactionSynced: Boolean,
)
