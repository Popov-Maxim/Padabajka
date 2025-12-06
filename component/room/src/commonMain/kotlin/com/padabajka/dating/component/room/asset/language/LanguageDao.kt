package com.padabajka.dating.component.room.asset.language

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.padabajka.dating.component.room.asset.language.entry.LanguageTranslationEntry

@Dao
interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguageTranslations(cities: List<LanguageTranslationEntry>)

    @Query(
        """
        SELECT * FROM language_translations
        WHERE language = :languageId
          AND id IN (
              SELECT id FROM language_translations
              WHERE name LIKE '%' || :query || '%' COLLATE NOCASE
          )
        ORDER BY name
    """
    )
    suspend fun findLangAssets(query: String, languageId: String): List<LanguageTranslationEntry>

    @Query(
        """
        SELECT * FROM language_translations
        WHERE id = :id and language = :language
        """
    )
    suspend fun getCityTranslation(id: String, language: String): LanguageTranslationEntry?
}
