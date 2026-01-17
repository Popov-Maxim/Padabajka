package com.padabajka.dating.component.room.asset.city

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.padabajka.dating.component.room.asset.city.entry.CityEntry

@Dao
interface CityDao {

    @Query("SELECT * FROM cities")
    suspend fun cities(): List<CityEntry>

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getCity(id: String): CityEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntry>)
}
