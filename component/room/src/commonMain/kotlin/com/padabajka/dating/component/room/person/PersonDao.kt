package com.padabajka.dating.component.room.person

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.padabajka.dating.component.room.person.entry.PersonEntry

@Dao
interface PersonDao {

    @Query("SELECT * FROM person WHERE id = :id")
    suspend fun getPerson(id: String): PersonEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(person: PersonEntry)

    @Query("DELETE FROM person WHERE id = :id")
    suspend fun delete(id: String)
}
