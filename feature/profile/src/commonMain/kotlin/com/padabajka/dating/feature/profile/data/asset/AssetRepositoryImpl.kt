package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.component.room.asset.entry.toDomain
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.core.repository.api.model.profile.asset.City
import com.padabajka.dating.feature.profile.data.asset.model.toDomain
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import kotlinx.coroutines.flow.first

class AssetRepositoryImpl(
    private val localAssetDataSource: LocalAssetsDataSource,
    private val localCityDataSource: LocalCityDataSource,
    private val settingsRepository: AppSettingsRepository,

    private val cityRepository: CityRepositoryImpl,
    private val languageRepository: LanguageAssetRepositoryImpl
) : AssetRepository {
    override suspend fun loadAssets() {
        cityRepository.loadCities()
        languageRepository.loadAssets()
    }

    override suspend fun findCities(query: String): List<City> {
        val language = settingsRepository.appSettings.first().language
        val assets = localAssetDataSource.find(
            type = Text.Type.City,
            query = query,
            language = language
        )

        return assets.mapNotNull { assets ->
            val cityEntry = localCityDataSource.getCity(assets.id)
            cityEntry?.toDomain(assets.name)
        }
    }

    override suspend fun findAssets(type: Text.Type, query: String): List<Text> {
        val language = settingsRepository.appSettings.first().language
        val assets = localAssetDataSource.find(
            type = Text.Type.Language,
            query = query,
            language = language
        )

        return assets.map { it.toDomain() }
    }

    override suspend fun getTranslation(
        text: Text,
        lang: Language
    ): String? {
        return localAssetDataSource.getTranslation(
            id = text.id.raw,
            type = text.type,
            language = lang
        )?.name
    }
}
