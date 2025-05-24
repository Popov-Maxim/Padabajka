package com.padabajka.dating.component.room.matches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.padabajka.dating.component.room.matches.entry.MatchEntry
import com.padabajka.dating.core.repository.api.model.auth.UserId
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao {

    @Query("SELECT * FROM matches ORDER BY creationTime DESC")
    fun matches(): Flow<List<MatchEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(matchEntry: MatchEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(matchesEntry: List<MatchEntry>)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun delete(id: String)

//    @Query("DELETE FROM matches WHERE userId = :userId")
//    suspend fun deleteByUserId(userId: String)

    @Query("DELETE FROM matches")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceMatchesForUser(userId: UserId, newMatches: List<MatchEntry>) {
        deleteAll()
        insertOrUpdate(newMatches)
    }
}
