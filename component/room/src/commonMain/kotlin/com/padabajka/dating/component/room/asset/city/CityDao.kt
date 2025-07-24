package com.padabajka.dating.component.room.asset.city

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.padabajka.dating.component.room.asset.city.entry.CityEntry
import com.padabajka.dating.component.room.asset.city.entry.CityTranslation

@Dao
interface CityDao {

    @Query("SELECT * FROM cities")
    suspend fun cities(): List<CityEntry>

    @Query("SELECT * FROM cities WHERE id = :id")
    suspend fun getCity(id: String): CityEntry?

    @Insert
    suspend fun insertCities(cities: List<CityEntry>)

    @Insert
    suspend fun insertCityTranslations(cities: List<CityTranslation>)

    @Query(
        """
        SELECT * FROM city_translations
        WHERE name LIKE '%' || :query || '%' COLLATE NOCASE
        """
    )
    suspend fun findCityTranslations(query: String): List<CityTranslation>

    @Query(
        """
        SELECT * FROM city_translations
        WHERE cityId = :id and language = :language
        """
    )
    suspend fun getCityTranslation(id: String, language: String): CityTranslation?
}
