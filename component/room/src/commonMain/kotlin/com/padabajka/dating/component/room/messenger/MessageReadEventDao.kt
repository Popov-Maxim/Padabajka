package com.padabajka.dating.component.room.messenger

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.padabajka.dating.component.room.messenger.entry.MessageReadEventEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageReadEventDao {

    @Query(
        """
        SELECT * FROM message_read_events
        WHERE chatId = :chatId
        """
    )
    fun readStatesByChatId(chatId: String): Flow<List<MessageReadEventEntry>>

    @Query(
        """
        SELECT *
        FROM message_read_events
        WHERE chatId = :chatId
        AND userId = :userId
        ORDER BY readAt DESC
        LIMIT 1
        """
    )
    suspend fun lastReadEvent(chatId: String, userId: String): MessageReadEventEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: MessageReadEventEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<MessageReadEventEntry>)

    @Query(
        """
        SELECT * FROM message_read_events
        WHERE id = :eventId
        LIMIT 1
        """
    )
    suspend fun getById(eventId: Long): MessageReadEventEntry?

    @Update
    suspend fun update(event: MessageReadEventEntry)
}
