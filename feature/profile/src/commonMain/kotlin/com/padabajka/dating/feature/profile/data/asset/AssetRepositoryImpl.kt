package com.padabajka.dating.feature.profile.data.asset

import com.padabajka.dating.component.room.asset.entry.AssetsTranslationEntry
import com.padabajka.dating.component.room.asset.entry.toDomain
import com.padabajka.dating.core.repository.api.AppSettingsRepository
import com.padabajka.dating.core.repository.api.AssetRepository
import com.padabajka.dating.core.repository.api.model.dictionary.Language
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.core.repository.api.model.profile.asset.City
import com.padabajka.dating.feature.profile.data.asset.model.AssetVersionsDto
import com.padabajka.dating.feature.profile.data.asset.model.toDomain
import com.padabajka.dating.feature.profile.data.asset.model.toEntities
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetVersionsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalAssetsDataSource
import com.padabajka.dating.feature.profile.data.asset.source.LocalCityDataSource
import com.padabajka.dating.feature.profile.data.asset.source.RemoteAssetDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AssetRepositoryImpl(
    private val localAssetDataSource: LocalAssetsDataSource,
    private val localCityDataSource: LocalCityDataSource,
    private val settingsRepository: AppSettingsRepository,
    private val remoteAssetDataSource: RemoteAssetDataSource,
    private val localAssetVersionsDataSource: LocalAssetVersionsDataSource,

    private val cityRepository: CityRepositoryImpl
) : AssetRepository {
    override suspend fun loadAssets(): Unit = coroutineScope { // TODO: add try catch for launch
        val versions = localAssetVersionsDataSource.versions()

        val cityJob = launch {
            cityRepository.loadCities(versions.cities)
        }

        val languageJob = launch {
            val languageDto = remoteAssetDataSource.loadLanguages(versions.languages) ?: return@launch
            val language = languageDto.assets.flatMap { it.toEntities() }
            setAssetTranslations(language, AssetVersionsDto.Type.Languages, languageDto.version)
        }

        val interestsJob = launch {
            val interestDto = remoteAssetDataSource.loadInterestAssets(versions.interests) ?: return@launch
            val interest = interestDto.assets.flatMap { it.toEntities() }
            setAssetTranslations(interest, AssetVersionsDto.Type.Interests, interestDto.version)
        }

        cityJob.join()
        languageJob.join()
        interestsJob.join()
    }

    private suspend fun setAssetTranslations(
        translations: List<AssetsTranslationEntry>,
        type: AssetVersionsDto.Type,
        newVersion: Int
    ) {
        localAssetDataSource.setTranslations(translations)
        localAssetVersionsDataSource.updateVersion(type, newVersion)
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
