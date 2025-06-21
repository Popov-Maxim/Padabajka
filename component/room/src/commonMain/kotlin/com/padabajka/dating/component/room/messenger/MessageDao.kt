package com.padabajka.dating.component.room.messenger

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntry>)

    @Update
    suspend fun updateMessage(message: MessageEntry)

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun messageById(id: String): MessageEntry

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY creationTime DESC")
    fun messagesByChatId(chatId: String): Flow<List<MessageEntry>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY creationTime DESC LIMIT 1")
    fun lastMessageByChatId(chatId: String): Flow<MessageEntry?>

    @Query(
        """
        SELECT COUNT(*)
        FROM messages
        WHERE chatId = :chatId AND readAt IS NULL AND authorId != :currentUserId 
        LIMIT 1
        """
    )
    suspend fun unreadMessagesCount(chatId: String, currentUserId: String): Int

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: String)

    @Query("DELETE FROM messages WHERE id IN (:ids)")
    suspend fun deleteMessagesByIds(ids: List<String>)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Transaction
    suspend fun replaceMessage(old: MessageEntry, new: MessageEntry) {
        deleteMessageById(old.id)
        insertMessage(new)
    }

    @Transaction
    suspend fun updateMessages(
        messagesForAdd: List<MessageEntry>,
        messageIdsForDelete: List<String>
    ) {
        insertMessages(messagesForAdd)
        deleteMessagesByIds(messageIdsForDelete)
    }
}
