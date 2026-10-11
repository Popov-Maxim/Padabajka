package com.padabajka.dating.component.room.messenger

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import com.padabajka.dating.component.room.messenger.entry.MessageVersionEntry
import com.padabajka.dating.core.repository.api.model.messenger.MessageStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntry>)

    @Update
    suspend fun updateMessage(message: MessageEntry)

    @Query(
        """
        UPDATE messages
        SET messageStatus = :status
        WHERE id = :id
          AND messageStatus != :excludedStatus
        """
    )
    suspend fun updateMessageStatusUnless(
        id: String,
        status: MessageStatus,
        excludedStatus: MessageStatus,
    )

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun messageById(id: String): MessageEntry

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun messageByIdOrNull(id: String): MessageEntry?

    @Query("SELECT id FROM messages WHERE chatId = :chatId AND editSynced = 0")
    suspend fun unsyncedEditMessageIds(chatId: String): List<String>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY creationTime DESC")
    fun messagesByChatId(chatId: String): Flow<List<MessageEntry>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY creationTime DESC LIMIT 1")
    fun lastMessageByChatId(chatId: String): Flow<MessageEntry?>

    @Query(
        """
        SELECT id
        FROM messages
        WHERE chatId = :chatId
        ORDER BY creationTime ASC LIMIT 1
        """
    )
    suspend fun oldestMessageByChatId(chatId: String): String?

    @Query(
        """
        SELECT COUNT(*)
        FROM messages
        WHERE chatId = :chatId
          AND authorId != :currentUserId
          AND creationTime > :lastReadTime
        """
    )
    suspend fun unreadMessagesCount(
        chatId: String,
        currentUserId: String,
        lastReadTime: Long
    ): Int

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: String)

    @Query("DELETE FROM messages WHERE id IN (:ids)")
    suspend fun deleteMessagesByIds(ids: List<String>)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Query("SELECT * FROM message_versions WHERE chatId = :chatId AND messageId = :messageId")
    suspend fun messageVersion(chatId: String, messageId: String): MessageVersionEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMessageVersion(entry: MessageVersionEntry)

    @Query("SELECT lastEventNumber FROM chats WHERE id = :chatId")
    suspend fun syncedMessageVersion(chatId: String): Long?

    @Transaction
    suspend fun applyRemoteChange(
        chatId: String,
        messageId: String,
        version: Long?,
        deleted: Boolean,
        fromEvent: Boolean,
        update: (MessageEntry?) -> MessageEntry?,
    ) {
        val saved = messageVersion(chatId, messageId)
        val current = messageByIdOrNull(messageId)
        // A sync snapshot may omit a deleted message entirely. Its cursor still covers the event.
        val isUnknownMessageEvent = fromEvent && !deleted && saved == null && current == null
        val coveredBySync = isUnknownMessageEvent && version != null &&
            version <= (syncedMessageVersion(chatId) ?: -1)
        if (!canApplyRemoteChange(saved, version, fromEvent) || coveredBySync) return

        if (deleted) {
            deleteMessageById(messageId)
        } else {
            val message = update(current) ?: return
            require(message.id == messageId && message.chatId == chatId)
            insertMessage(message)
        }
        saveMessageVersion(MessageVersionEntry(chatId, messageId, version, deleted))
    }

    @Transaction
    suspend fun updateMessage(messageId: String, update: (MessageEntry) -> MessageEntry): MessageEntry {
        val old = messageById(messageId)
        val updated = update(old)
        require(updated.id == old.id && updated.chatId == old.chatId)
        updateMessage(updated)
        return updated
    }
}

private fun canApplyRemoteChange(
    saved: MessageVersionEntry?,
    version: Long?,
    fromEvent: Boolean,
): Boolean {
    val savedVersion = saved?.version
    // Equal HTTP/sync snapshots can confirm or roll back optimistic local changes.
    return when {
        saved?.deleted == true -> false
        savedVersion == null -> true
        version == null -> false
        fromEvent -> version > savedVersion
        else -> version >= savedVersion
    }
}
