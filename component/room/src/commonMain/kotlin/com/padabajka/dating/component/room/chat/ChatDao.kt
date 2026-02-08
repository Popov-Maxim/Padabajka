package com.padabajka.dating.component.room.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.padabajka.dating.component.room.chat.entry.ChatEntry

@Dao
interface ChatDao {

    @Query("SELECT * FROM chats")
    suspend fun getChats(): List<ChatEntry>

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChat(chatId: String): ChatEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(chatEntry: ChatEntry)

    @Query("SELECT lastEventNumber FROM chats WHERE id = :chatId")
    suspend fun getLastEventNumber(chatId: String): Long?

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteById(chatId: String)
}
