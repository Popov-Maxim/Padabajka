package com.padabajka.dating.component.room.session

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserSessionDao {

    @Transaction
    suspend fun clearUserData() {
        deleteMessageReadEvents()
        deleteMessages()
        deleteChats()
        deleteMatches()
        deletePersons()
    }

    @Query("DELETE FROM messages")
    suspend fun deleteMessages()

    @Query("DELETE FROM message_read_events")
    suspend fun deleteMessageReadEvents()

    @Query("DELETE FROM chats")
    suspend fun deleteChats()

    @Query("DELETE FROM matches")
    suspend fun deleteMatches()

    @Query("DELETE FROM person")
    suspend fun deletePersons()
}
