package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.feature.profile.data.asset.model.AssetsDto
import com.padabajka.dating.feature.profile.data.asset.model.CityDto
import com.padabajka.dating.feature.profile.data.asset.network.CityApi

class RemoteCityDataSourceImpl(
    private val cityApi: CityApi
) : RemoteCityDataSource {
    override suspend fun loadCities(clientVersion: Int): AssetsDto<CityDto>? {
        return cityApi.getCities(clientVersion)
    }
}
