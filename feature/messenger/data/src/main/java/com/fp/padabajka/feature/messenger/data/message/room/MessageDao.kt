package com.fp.padabajka.feature.messenger.data.message.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insertMessage(message: MessageEntry)

    @Update
    suspend fun updateMessage(message: MessageEntry)

    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun messageById(id: String): MessageEntry

    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    fun messagesByChatId(chatId: String): Flow<List<MessageEntry>>

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY creationTime DESC LIMIT 1")
    suspend fun lastMessageByChatId(chatId: String): MessageEntry?

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
