package com.padabajka.dating.component.room.matches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.padabajka.dating.component.room.matches.entry.MatchEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao {

    @Query("SELECT * FROM matches ORDER BY creationTime DESC")
    fun matches(): Flow<List<MatchEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(matchEntry: MatchEntry)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun delete(id: String)
}
