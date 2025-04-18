package com.padabajka.dating.component.room.messenger

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.padabajka.dating.component.room.messenger.entry.MessageEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntry)

    @Update
    suspend fun updateMessage(message: MessageEntry)

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun messageById(id: String): MessageEntry

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY creationTime DESC")
    fun messagesByChatId(chatId: String): Flow<List<MessageEntry>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY creationTime DESC LIMIT 1")
    fun lastMessageByChatId(chatId: String): Flow<MessageEntry?>

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: String)

    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesByChatId(chatId: String)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Transaction
    suspend fun replaceMessage(old: MessageEntry, new: MessageEntry) {
        deleteMessageById(old.id)
        insertMessage(new)
    }
}
