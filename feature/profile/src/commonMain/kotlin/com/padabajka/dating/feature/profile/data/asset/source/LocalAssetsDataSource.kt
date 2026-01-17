package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.component.room.asset.AssetsDao
import com.padabajka.dating.component.room.asset.entry.AssetsTranslationEntry
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text

class LocalAssetsDataSource(
    private val assetsDao: AssetsDao,
) {
    suspend fun setTranslations(assets: List<AssetsTranslationEntry>) {
        assetsDao.insertAssetsTranslations(assets)
    }

    suspend fun find(
        type: Text.Type,
        query: String,
        language: Language
    ): List<AssetsTranslationEntry> {
        return assetsDao.findAssets(
            type = type.raw,
            query = query,
            languageId = language.id
        )
    }

    suspend fun getTranslation(
        id: String,
        type: Text.Type,
        language: Language
    ): AssetsTranslationEntry? {
        return assetsDao.getCityTranslation(
            type = type.raw,
            id = id,
            language = language.id
        )
    }
}
