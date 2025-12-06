package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.component.room.asset.language.entry.toDomain
import com.padabajka.dating.core.repository.api.LanguageAssetRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.data.asset.model.toEntities
import com.padabajka.dating.feature.profile.data.asset.source.LocalLanguageAssetDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteLanguageAssetDataSource

class LanguageAssetRepositoryImpl(
    private val remoteLanguageAssetDataSource: RemoteLanguageAssetDataSource,
    private val localLanguageAssetDataSource: LocalLanguageAssetDataSource
) : LanguageAssetRepository {
    override suspend fun loadAssets() {
        val languagesDto = remoteLanguageAssetDataSource.loadLanguages()
        val entries = languagesDto.flatMap { it.toEntities() }
        localLanguageAssetDataSource.setTranslations(entries)
    }

    override suspend fun findAssets(query: String): List<Text> {
        val entries = localLanguageAssetDataSource.find(query, Language.Static.EN)
        return entries.map { it.toDomain() }
    }

    override suspend fun getTranslation(
        id: String,
        lang: Language
    ): String? {
        return localLanguageAssetDataSource.getLanguage(id, lang)?.name
    }
}
