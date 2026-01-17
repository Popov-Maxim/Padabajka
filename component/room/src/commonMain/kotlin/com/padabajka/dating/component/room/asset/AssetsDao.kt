package com.padabajka.dating.component.room.asset

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.padabajka.dating.component.room.asset.entry.AssetsTranslationEntry

@Dao
interface AssetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssetsTranslations(assets: List<AssetsTranslationEntry>)

    @Query(
        """
        SELECT * FROM assets_translation
        WHERE language = :languageId
          AND type = :type
          AND id IN (
              SELECT id FROM assets_translation
              WHERE type = :type
                AND name LIKE '%' || :query || '%' COLLATE NOCASE
          )
        ORDER BY name
    """
    )
    suspend fun findAssets(type: String, query: String, languageId: String): List<AssetsTranslationEntry>

    @Query(
        """
        SELECT * FROM assets_translation
        WHERE id = :id and type = :type and language = :language
        """
    )
    suspend fun getCityTranslation(type: String, id: String, language: String): AssetsTranslationEntry?
}
