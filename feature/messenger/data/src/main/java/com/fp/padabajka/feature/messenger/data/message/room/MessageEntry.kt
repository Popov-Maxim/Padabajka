package com.fp.padabajka.feature.messenger.data.message.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fp.padabajka.core.repository.api.model.messenger.MessageReaction
import com.fp.padabajka.core.repository.api.model.messenger.MessageStatus

@Entity(tableName = "messages")
class MessageEntry(
    @PrimaryKey val id: String,
    @ColumnInfo("chatId") val chatId: String,
    @ColumnInfo("authorId") val authorId: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("creationTime") val creationTime: Long,
    @ColumnInfo("reaction") val reaction: MessageReaction?,
    @ColumnInfo("reactionSynced") val reactionSynced: Boolean,
    @ColumnInfo("messageStatus") val messageStatus: MessageStatus,
    @ColumnInfo("readSynced") val readSynced: Boolean,
    @ColumnInfo("parentMessageId") val parentMessageId: String?
)
