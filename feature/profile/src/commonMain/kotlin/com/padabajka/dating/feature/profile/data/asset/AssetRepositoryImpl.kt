package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.component.room.asset.entry.toDomain
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.core.repository.api.model.profile.asset.City
import com.padabajka.dating.feature.profile.data.asset.model.toDomain
import com.padabajka.dating.feature.profile.data.asset.model.toEntities
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteAssetDataSource
import kotlinx.coroutines.flow.first

class AssetRepositoryImpl(
    private val localAssetDataSource: LocalAssetsDataSource,
    private val localCityDataSource: LocalCityDataSource,
    private val settingsRepository: AppSettingsRepository,
    private val remoteAssetDataSource: RemoteAssetDataSource,

    private val cityRepository: CityRepositoryImpl
) : AssetRepository {
    override suspend fun loadAssets() {
        // TODO: add async
        val cities = cityRepository.loadCities()
        val language = remoteAssetDataSource.loadLanguages().flatMap { it.toEntities() }
        val interest = remoteAssetDataSource.loadInterestAssets().flatMap { it.toEntities() }
        localAssetDataSource.setTranslations(cities + language + interest)
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
            type = type,
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
