package com.padabajka.dating.feature.profile.data.asset.source

import androidx.datastore.core.DataStore
import com.padabajka.dating.feature.profile.data.asset.model.AssetVersionsDto
import kotlinx.coroutines.flow.first

class LocalAssetVersionsDataSource(
    private val assetVersionsDataStore: DataStore<AssetVersionsDto>
) {

    suspend fun versions(): AssetVersionsDto {
        return getAssetVersions()
    }

    suspend fun updateVersion(type: AssetVersionsDto.Type, version: Int) {
        assetVersionsDataStore.updateData {
            when (type) {
                AssetVersionsDto.Type.Cities -> it.copy(cities = version)
                AssetVersionsDto.Type.Languages -> it.copy(languages = version)
                AssetVersionsDto.Type.Interests -> it.copy(interests = version)
            }
        }
    }

    private suspend fun getAssetVersions(): AssetVersionsDto {
        return assetVersionsDataStore.data.first()
    }
}
