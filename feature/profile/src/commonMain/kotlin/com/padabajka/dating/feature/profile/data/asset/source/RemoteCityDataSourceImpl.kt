package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.feature.profile.data.asset.model.CityDto
import com.padabajka.dating.feature.profile.data.asset.network.CityApi

class RemoteCityDataSourceImpl(
    private val cityApi: CityApi
) : RemoteCityDataSource {
    override suspend fun loadCities(): List<CityDto> {
        return cityApi.getCities()
    }
}
